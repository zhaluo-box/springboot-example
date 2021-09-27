package com.zl.box.bootmybaits.service.impl;

import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import com.zl.box.bootmybaits.common.BaseQuery;
import com.zl.box.bootmybaits.common.Pager;
import com.zl.box.bootmybaits.mapper.AccountMapper;
import com.zl.box.bootmybaits.mapper.UserMapper;
import com.zl.box.bootmybaits.model.User;
import com.zl.box.bootmybaits.model.dto.AccountUserDto;
import com.zl.box.bootmybaits.service.IUserService;
import org.apache.poi.openxml4j.exceptions.InvalidFormatException;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.List;

@Service
public class UserServiceImpl implements IUserService {

    @Autowired
    private UserMapper userMapper;

    @Override
    public User getUser(String id) {
        return userMapper.selectByPrimaryKey( id );
    }

    @Override
    public List<User> findAll() {
        return userMapper.selectAll();
    }

    /**
     * 添加用户.
     * @param users
     * @return
     */
    @Override
    public String save(List<User> users) {
        final int i = userMapper.insertList( users );
        if (i > 0) {
            return "用户添加成功";
        }
        return "用户添加失败";
    }

    /**
     * 根据性别查询用户
     *
     * @param params
     * @return
     */
    @Override
    public PageInfo<User> findUserBySex(BaseQuery<String> params) {
        final String sex = params.getEntity();
        final Pager page = params.getPager();
        PageHelper.startPage( page.getPageIndex(), page.getPageSize() );
        PageInfo<User> pageList = new PageInfo<User>( userMapper.selectBySex( sex ) );
        return pageList;
    }

    @Override
    public PageInfo<User> selectUserLikeUsername(BaseQuery<String> username) {
        final Pager pager = username.getPager();
        PageHelper.startPage( pager.getPageIndex(), pager.getPageSize() );
        final String params = username.getEntity();
        PageInfo<User> pageList = new PageInfo( userMapper.selectUserLikeUsername( params ) );
        return pageList;
    }

    @Autowired
    private AccountMapper accountMapper;

    /**
     * poi 文件下载
     *
     * @param request
     * @param response
     */
    @Override
    public void exportExcelByTemplate(HttpServletRequest request, HttpServletResponse response) {
        // 获取模板文件路径
        String templatePath = "C:\\data\\template.xlsx";
        ServletOutputStream out = null;
        try {
            response.setContentType( "application/xlsx" );
            response.setHeader( "Content-Disposition", "attachment; filename=AAA.xlsx" );
            // 获取数据
            List<AccountUserDto> acclist = accountMapper.findAll2();
            List<User> userList = userMapper.selectAll();
            // 拿到模板
            // POIFSFileSystem fs = new POIFSFileSystem( new FileInputStream( templatePath ) );
            // FileInputStream tps = new FileInputStream( new File( templatePath ) );
            // 获取模板
            // HSSFWorkbook tpWorkbook = new HSSFWorkbook( fs );
            XSSFWorkbook tpWorkbook = new XSSFWorkbook( new File( templatePath ) );
            // 获取sheet页
            XSSFSheet sheet1 = tpWorkbook.getSheet( "用户表" );
            int index = 1;
            for (User user : userList) {
                XSSFRow row = sheet1.getRow( index );
                if (null == row) {
                    row = sheet1.createRow( index );
                }
                XSSFCell cell0 = row.createCell( 0 );
                XSSFCell cell1 = row.createCell( 1 );
                XSSFCell cell2 = row.createCell( 2 );
                XSSFCell cell3 = row.createCell( 3 );
                XSSFCell cell4 = row.createCell( 4 );
                cell0.setCellValue( user.getId() );
                cell1.setCellValue( user.getUsername() );
                DateFormat format = new SimpleDateFormat( "yyyy-MM-dd HH:mm:ss" );
                cell2.setCellValue( format.format( user.getBirthday() ) );
                cell3.setCellValue( user.getSex() );
                cell4.setCellValue( user.getAddress() );
                index++;
            }
            XSSFSheet sheet2 = tpWorkbook.getSheetAt( 1 );
            index = 1;
            for (AccountUserDto acc : acclist) {
                XSSFRow row = sheet2.getRow( index );
                if (null == row) {
                    row = sheet2.createRow( index );
                }
                XSSFCell cell0 = row.createCell( 0 );
                XSSFCell cell1 = row.createCell( 1 );
                XSSFCell cell2 = row.createCell( 2 );
                XSSFCell cell3 = row.createCell( 3 );
                XSSFCell cell4 = row.createCell( 4 );
                cell0.setCellValue( acc.getId() );
                cell1.setCellValue( acc.getUid() );
                cell2.setCellValue( acc.getMoney() );
                cell3.setCellValue( acc.getUsername() );
                cell4.setCellValue( acc.getAddress() );
                index++;
            }
            //            File file = new File("C:\\home\\AAA.xlsx");
            //            File marks = new File(file.getParent());
            //            if (!marks.exists()) {
            //                marks.mkdirs();
            //            }
            //            if (!file.exists()) {
            //                file.createNewFile();
            //            }
            //            FileOutputStream fileOut = new FileOutputStream(file);
            out = response.getOutputStream();
            tpWorkbook.write( out );
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (InvalidFormatException e) {
            e.printStackTrace();
        } finally {
            try {
                if (null != out) {
                    out.close();
                    System.out.println("流关闭");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
