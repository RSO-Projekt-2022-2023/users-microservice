package si.fri.rso.project.user.graphql;

import com.kumuluz.ee.graphql.annotations.GraphQLClass;
import com.kumuluz.ee.graphql.classes.Filter;
import com.kumuluz.ee.graphql.classes.Pagination;
import com.kumuluz.ee.graphql.classes.PaginationWrapper;
import com.kumuluz.ee.graphql.classes.Sort;
import com.kumuluz.ee.graphql.utils.GraphQLUtils;
import io.leangen.graphql.annotations.GraphQLArgument;
import io.leangen.graphql.annotations.GraphQLQuery;
import si.fri.rso.project.user.lib.User;
import si.fri.rso.project.user.services.beans.UserBean;

import javax.enterprise.context.ApplicationScoped;
import javax.inject.Inject;

@GraphQLClass
@ApplicationScoped
public class UserQueries {

    @Inject
    private UserBean userBean;

    @GraphQLQuery
    public PaginationWrapper<User> allUsers(@GraphQLArgument(name = "pagination") Pagination pagination,
                                                    @GraphQLArgument(name = "sort") Sort sort,
                                                    @GraphQLArgument(name = "filter") Filter filter) {

        return GraphQLUtils.process(userBean.getUser(), pagination, sort, filter);
    }

    @GraphQLQuery
    public User getUser(@GraphQLArgument(name = "id") Integer id) {
        return userBean.getUser(id);
    }

}
