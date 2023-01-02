package si.fri.rso.project.user.services.beans;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.TypedQuery;
import javax.ws.rs.NotFoundException;
import javax.ws.rs.core.UriInfo;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import com.kumuluz.ee.rest.beans.QueryParameters;
import com.kumuluz.ee.rest.utils.JPAUtils;

import si.fri.rso.project.user.lib.User;
import si.fri.rso.project.user.models.converters.UserConverter;
import si.fri.rso.project.user.models.entities.UserEntity;


@RequestScoped
public class UserBean {

    private Logger log = Logger.getLogger(UserBean.class.getName());

    @Inject
    private EntityManager em;

    public List<User> getUser() {

        TypedQuery<UserEntity> query = em.createNamedQuery(
                "UserEntity.getAll", UserEntity.class);

        List<UserEntity> resultList = query.getResultList();

        return resultList.stream().map(UserConverter::toDto).collect(Collectors.toList());

    }

    public List<User> getUserFilter(UriInfo uriInfo) {

        QueryParameters queryParameters = QueryParameters.query(uriInfo.getRequestUri().getQuery()).defaultOffset(0)
                .build();

        return JPAUtils.queryEntities(em, UserEntity.class, queryParameters).stream()
                .map(UserConverter::toDto).collect(Collectors.toList());
    }

    public User getUser(Integer id) {

        UserEntity userEntity = em.find(UserEntity.class, id);

        if (userEntity == null) {
            throw new NotFoundException();
        }

        User user = UserConverter.toDto(userEntity);

        return user;
    }

    public User userLogin(User user){
        TypedQuery<UserEntity> query = em.createNamedQuery(
                "UserEntity.userLogin", UserEntity.class).setParameter(1, user.getUsername()).setParameter(2, user.getPassword());

        List<UserEntity> userEntityList = query.getResultList();
        if(userEntityList.isEmpty()){
            return null;
        }
        User user1 = UserConverter.toDto(userEntityList.get(0));
        return user1;

    }

    public User createUser(User user) {

        UserEntity userEntity = UserConverter.toEntity(user);

        try {
            beginTx();
            em.persist(userEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        if (userEntity.getId() == null) {
            throw new RuntimeException("Entity was not persisted");
        }

        return UserConverter.toDto(userEntity);
    }

    public User putUser(Integer id, User user) {

        UserEntity c = em.find(UserEntity.class, id);

        if (c == null) {
            return null;
        }

        UserEntity updatedUserEntity = UserConverter.toEntity(user);

        try {
            beginTx();
            updatedUserEntity.setId(c.getId());
            updatedUserEntity = em.merge(updatedUserEntity);
            commitTx();
        }
        catch (Exception e) {
            rollbackTx();
        }

        return UserConverter.toDto(updatedUserEntity);
    }

    public boolean deleteUser(Integer id) {

        UserEntity user = em.find(UserEntity.class, id);

        if (user != null) {
            try {
                beginTx();
                em.remove(user);
                commitTx();
            }
            catch (Exception e) {
                rollbackTx();
            }
        }
        else {
            return false;
        }

        return true;
    }

    private void beginTx() {
        if (!em.getTransaction().isActive()) {
            em.getTransaction().begin();
        }
    }

    private void commitTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().commit();
        }
    }

    private void rollbackTx() {
        if (em.getTransaction().isActive()) {
            em.getTransaction().rollback();
        }
    }
}
