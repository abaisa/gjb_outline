package test;


import cn.gjb151b.outline.model.ViewProject;
import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.InputStream;

public class test_MD5 {
    public static void main(String args[]){
        try{
            String resource = "mybatis.develop.xml";
            InputStream inputStream = Resources.getResourceAsStream(resource);
            SqlSessionFactory factory = new SqlSessionFactoryBuilder().build(inputStream);
            SqlSession session = factory.openSession();
            ViewProject viewProject =  new ViewProject();
            viewProject = session.selectOne("mapper.Project.selectProjectByItemId","91c747655930407585003425f42811b8");
            if(viewProject== null){
                System.out.println("0");

            }else {
                System.out.println(viewProject.getDevName());
            }


        }catch (Exception e){
            e.printStackTrace();

        }
    }



}
