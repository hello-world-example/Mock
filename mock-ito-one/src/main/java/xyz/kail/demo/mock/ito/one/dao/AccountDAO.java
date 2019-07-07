package xyz.kail.demo.mock.ito.one.dao;

import net.paoding.rose.jade.annotation.DAO;
import net.paoding.rose.jade.annotation.SQL;
import net.paoding.rose.jade.annotation.SQLParam;
import xyz.kail.demo.mock.ito.one.model.AccountVO;

@DAO
public interface AccountDAO {

    @SQL("SELECT UNAME,PASSWD FROM T_ACCOUNT WHERE 1=1 " +
            " #if(null != :vo.uname){ AND UNAME = :vo:uname }" +
            " #if(null != :vo.passwd){ AND PASSWD = :vo:passwd }" +
            " LIMIT 1")
    AccountVO selectAccountByName(@SQLParam("vo") AccountVO vo);

}
