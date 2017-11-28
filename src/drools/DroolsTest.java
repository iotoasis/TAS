package drools;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;

import org.kie.api.KieBase;
import org.kie.api.io.ResourceType;
import org.kie.api.runtime.KieSession;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.springframework.web.servlet.ModelAndView;

import com.common.SQLManager;
import com.ibatis.sqlmap.client.SqlMapClient;

public class DroolsTest {
	static String droolsTest = "tas-sql.droolsTest";
	static SqlMapClient sqlMapper = null;
	
	public static final void main(String[] args) {

		try {

			if(sqlMapper == null){
				sqlMapper = SQLManager.sqlMapper; 
			}

			ArrayList resultList = new ArrayList<>();


			resultList = (ArrayList)sqlMapper.queryForList(droolsTest,null);

			HashMap map = new HashMap<>();
			
			map = (HashMap) resultList.get(0);
			
			
			
			KnowledgeBuilder kbuilder = KnowledgeBuilderFactory.newKnowledgeBuilder();
			//                     kbuilder.add(ResourceFactory.newClassPathResource(".resource.drl"), ResourceType. DRL );
//			kbuilder.add(ResourceFactory.newFileResource("C:/grib_project/ICBMS/ICBMS_TAS/src/drools/resource.drl"), ResourceType. DRL );
			
			
			
			String str = map.get("content").toString();

			// convert String into InputStream
			InputStream is = new ByteArrayInputStream(str.getBytes());
			
			System.out.println(is.toString());
			
			kbuilder.add(ResourceFactory.newInputStreamResource(is), ResourceType. DRL );
			
			KieBase kiebase = kbuilder.newKnowledgeBase();
			KieSession session = kiebase.newKieSession();

			session.insert( new BusinessTrip("사용자1" ,3.5));
			session.insert( new BusinessTrip("사용자2" ,5.0));
			session.insert( new BusinessTrip("사용자3" ,8.0));
			session.insert( new BusinessTrip("사용자4" ,10.0));
			session.insert( new BusinessTrip("사용자5" ,13.0));

			session.fireAllRules();

		} catch (Throwable t) {
			t.printStackTrace();
		}
	}
}


