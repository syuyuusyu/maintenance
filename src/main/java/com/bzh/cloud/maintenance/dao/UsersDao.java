package com.bzh.cloud.maintenance.dao;

import com.bzh.cloud.maintenance.entity.Users;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UsersDao extends PagingAndSortingRepository<Users,String> {

}
