package com.jc.service.impl;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.crypto.SecureUtil;
import com.auth0.jwt.JWT;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.jc.common.Constants;
import com.jc.common.RoleEnum;
import com.jc.controller.dto.EmployeeDTO;
import com.jc.controller.dto.PasswordDTO;
import com.jc.controller.dto.StudentDTO;
import com.jc.entity.Employee;
import com.jc.entity.Menu;
import com.jc.exception.ServiceException;
import com.jc.mapper.EmployeeMapper;
import com.jc.mapper.RoleMapper;
import com.jc.mapper.RoleMenuMapper;
import com.jc.service.EmployeeService;
import com.jc.untils.TokenUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.DigestUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class EmployeeServiceImpl extends ServiceImpl<EmployeeMapper, Employee> implements EmployeeService {

    @Resource
    private EmployeeMapper employeeMapper;

    @Resource
    private RoleMapper roleMapper;

    @Resource
    private RoleMenuMapper roleMenuMapper;

    @Resource MenuServiceImpl menuService;

    @Override
    public EmployeeDTO login(EmployeeDTO employeeDTO) {
        //1.将页面提交的密码password进行md5加密处理
        String password = employeeDTO.getPassword();
        password = DigestUtils.md5DigestAsHex(password.getBytes());
        //2.根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employeeDTO.getUsername());
        Employee emp = this.getOne(queryWrapper);

        //3.如果没有查询到则返回登陆失败结果
        if (emp == null){
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
        //4.密码比对，如果不一致则返回登陆失败结果
        if (!emp.getPassword().equals(password)){
            throw new ServiceException(Constants.CODE_600, "用户名或密码错误");
        }
        //5.查看员工状态，如果为已禁用状态，则返回员工已禁用结果
        if (emp.getStatus() == 0){
            throw new ServiceException(Constants.CODE_600, "员工已禁用");
        }
        //6.登录成功，将员工id存入并返回登录成功结果
        BeanUtils.copyProperties(emp, employeeDTO);
        String token = TokenUtils.genToken(emp.getId().toString(), emp.getPassword());
        employeeDTO.setToken(token);

        String role = emp.getRole();// ROLE_ADMIN
        // 设置用户的菜单列表
        List<Menu> roleMenus = getRoleMenus(role);
        employeeDTO.setMenus(roleMenus);

        return employeeDTO;
    }

    @Override
    public Employee saveAll(Employee employee) {
        //设置初始密码为123456，需要进行md5加密处理
        employee.setPassword(DigestUtils.md5DigestAsHex("123456".getBytes()));

        //根据页面提交的用户名username查询数据库
        LambdaQueryWrapper<Employee> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(Employee::getUsername,employee.getUsername());
        Employee emp = this.getOne(queryWrapper);

//        if (emp!=null){
//            throw new ServiceException(Constants.CODE_600, "用户名已存在");
//        }


        //获取当前登录用户
        Employee currentUser = TokenUtils.getCurrentUser();
        String empName = currentUser.getUsername();
        employee.setCreateUser(empName);
        employee.setUpdateUser(empName);

        this.saveOrUpdate(employee);

        return employee;
    }

    @Override
    public Employee updateWithId(Employee employee) {

        //获取当前登录用户
        Employee currentUser = TokenUtils.getCurrentUser();
        String empName = currentUser.getUsername();
        employee.setCreateUser(empName);
        employee.setUpdateUser(empName);

        this.updateById(employee);

        return employee;
    }

    @Override
    public Page<StudentDTO> findPage(Page<StudentDTO> page, String username,Integer isDelete,String planName) {
        return employeeMapper.findPage(page,username,isDelete,planName);
    }

    @Override
    public Employee register(EmployeeDTO employeeDTO) {
        // 用户密码 md5加密
        employeeDTO.setPassword(SecureUtil.md5(employeeDTO.getPassword()));
        Employee one = getUserInfo(employeeDTO);
        if (one == null) {
            one = new Employee();
            BeanUtil.copyProperties(employeeDTO, one, true);
            // 默认一个普通用户的角色
            one.setRole(RoleEnum.ROLE_STUDENT.toString());
            if (one.getName() == null) {
                one.setName(one.getUsername());
            }
            save(one);  // 把 copy完之后的用户对象存储到数据库
        } else {
            throw new ServiceException(Constants.CODE_600, "用户已存在");
        }
        return one;
    }

    @Override
    public void updatePassword(PasswordDTO passwordDTO) {
        int update = employeeMapper.updatePassword(passwordDTO);
        if (update < 1) {
            throw new ServiceException(Constants.CODE_600, "原密码错误");
        }
    }

    /**
     * 查询是否有相同用户名密码
     * @param employeeDTO
     * @return
     */
    private Employee getUserInfo(EmployeeDTO employeeDTO) {
        QueryWrapper<Employee> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("username", employeeDTO.getUsername());
//        queryWrapper.eq("password", employeeDTO.getPassword());
        Employee one;
        try {
            one = getOne(queryWrapper); // 从数据库查询用户信息
        } catch (Exception e) {
//            LOG.error(e);
            throw new ServiceException(Constants.CODE_500, "系统错误");
        }
        return one;
    }

    /**
     * 获取当前角色的菜单列表
     * @param roleFlag
     * @return
     */
    private List<Menu> getRoleMenus(String roleFlag) {
        Integer roleId = roleMapper.selectByFlag(roleFlag);
        // 当前角色的所有菜单id集合
        List<Integer> menuIds = roleMenuMapper.selectByRoleId(roleId);

        // 查出系统所有的菜单(树形)
        List<Menu> menus = menuService.findMenus("");
        // new一个最后筛选完成之后的list
        List<Menu> roleMenus = new ArrayList<>();
        // 筛选当前用户角色的菜单
        for (Menu menu : menus) {
            if (menuIds.contains(menu.getId())) {
                roleMenus.add(menu);
            }
            List<Menu> children = menu.getChildren();
            // removeIf()  移除 children 里面不在 menuIds集合中的 元素
            children.removeIf(child -> !menuIds.contains(child.getId()));
        }
        return roleMenus;
    }
}
