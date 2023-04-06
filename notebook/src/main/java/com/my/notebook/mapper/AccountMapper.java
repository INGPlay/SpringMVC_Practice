package com.my.notebook.mapper;

import com.my.notebook.domain.AccountDTO;
import com.my.notebook.domain.EncodedAccountDTO;
import com.my.notebook.domain.account.EncodedLoginDTO;
import com.my.notebook.domain.account.LoginDTO;
import org.apache.ibatis.annotations.*;
import org.springframework.stereotype.Repository;

import java.util.List;

@Mapper
@Repository
public interface AccountMapper {
    @Select("Select * from account_tbl")
    List<AccountDTO> selectAll();

    @Select("select * from account_tbl where a_id = ${id}")
    AccountDTO selectById(long id);

    @Select("select * from account_tbl where a_username = #{username}")
    EncodedAccountDTO selectEncodedAccountByUsername(String username);

    @Insert("insert into account_tbl(a_id, a_username, a_password, a_encodedPassword, a_isAdmin, a_created)" +
            "VALUES (a_id_seq.nextval, #{username}, #{password}, #{encodedPassword}, '1', sysdate)")
    void insertAccount(EncodedLoginDTO encodedLoginDTO);

    @Delete("delete from account_tbl where a_id = ${id}")
    void deleteById(long id);

    @Delete("delete from account_tbl where a_username = #{username}")
    void deleteByUsername(String username);

    @Update("update account_tbl SET a_password = #{password}" +
            "where a_id = ${id}")
    void updatePasswordById(@Param("id") long id, @Param("password") String password);

    @Update("update account_tbl SET a_password = #{password}" +
            "where a_username = #{username}")
    void updatePasswordByUsername(@Param("username")String username, @Param("password") String password);
}
