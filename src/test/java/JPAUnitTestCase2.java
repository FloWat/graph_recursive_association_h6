import com.test.hibernate6bug.model.lazybug.EntityNode;
import com.test.hibernate6bug.model.lazybug.EntityNode_;
import com.test.hibernate6bug.model.lazybug.EntitySimple;
import jakarta.persistence.EntityGraph;
import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import jakarta.persistence.Subgraph;
import java.util.List;
import org.hibernate.Hibernate;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * This template demonstrates how to develop a test case for Hibernate ORM, using the Java Persistence API.
 */
public class JPAUnitTestCase2 {

    private EntityManagerFactory entityManagerFactory;

    @Before
    public void init() {
        entityManagerFactory = Persistence.createEntityManagerFactory( "templatePU" );
    }

    @After
    public void destroy() {
        entityManagerFactory.close();
    }

    // Entities are auto-discovered, so just add them anywhere on class-path
    // Add your tests, using standard JUnit.
    @Test
    public void findTest() throws Exception {
        EntityManager entityManager = entityManagerFactory.createEntityManager();
        entityManager.getTransaction().begin();

        entityManager.createNativeQuery("insert into EntityNode(id) values(1)")
          .executeUpdate();
        entityManager.createNativeQuery("insert into EntityGroup(id,groupName) values(1,'nameGroup1')")
                     .executeUpdate();
        entityManager.createNativeQuery("insert into EntityNode(id,parent_id) values(2,1)")
                     .executeUpdate();
        entityManager.createNativeQuery("insert into EntityGroup(id,groupName) values(2,'nameGroup2')")
                     .executeUpdate();
        entityManager.createNativeQuery("insert into EntityNode(id,parent_id) values(3,2)")
                     .executeUpdate();
        entityManager.createNativeQuery("insert into EntitySimple(id,name) values(3,'name3')")
                     .executeUpdate();

        entityManager.getTransaction().commit();

        entityManager.getTransaction().begin();
        EntityGraph<EntitySimple> graph = entityManager.createEntityGraph(EntitySimple.class);
        Subgraph<EntityNode> subgraph1 = graph.addSubgraph(EntityNode_.parent.getName(), EntityNode.class);
        Subgraph<EntityNode> subgraph2 = subgraph1.addSubgraph(EntityNode_.parent.getName(), EntityNode.class);
        subgraph2.addSubgraph(EntityNode_.parent.getName(), EntityNode.class);

        List<EntitySimple> listWithGraph = entityManager.createQuery("select e from EntitySimple e "
            , EntitySimple.class).setHint("jakarta.persistence.fetchgraph",graph).getResultList();

        entityManager.getTransaction().commit();
        entityManager.close();

        for (EntitySimple a : listWithGraph) {
            EntityNode en = a;
            while(en.getParent() != null){
                Assert.assertTrue(Hibernate.isInitialized(en.getParent()));
                en = en.getParent();
            }
        }

    }
}