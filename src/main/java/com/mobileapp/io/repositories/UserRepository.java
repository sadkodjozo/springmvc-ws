package com.mobileapp.io.repositories;


import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import com.mobileapp.io.entity.UserEntity;


@Repository
public interface UserRepository extends PagingAndSortingRepository<UserEntity, Long> {
	UserEntity findByEmail(String email);
	UserEntity findByUserId(String userId);
	UserEntity findUserByEmailVerificationToken(String token);
	
	@Query(value="select * from Users u where u.EMAIL_VERIFICATION_STATUS = TRUE ",
			countQuery="select count(*) from Users u where u.EMAIL_VERIFICATION_STATUS = true", 
			nativeQuery = true)
	Page<UserEntity> findAllUsersWithConfirmedEmailAddress( Pageable pageableRequest );
	
	
	@Query(value="select * from Users u where u.first_name = ?1", nativeQuery = true)
	List<UserEntity> findUserByFirstName(String firstName);
	
	@Query(value="select * from Users u where u.last_name=:lastName", nativeQuery = true)
	List<UserEntity> findUserByLastName(@Param("lastName") String lastName);
	
	@Query(value="select * from Users u where first_name like %:keyword% or last_name like %:keyword%  ", nativeQuery = true)
	List<UserEntity> findUserByKeyword(@Param("keyword") String keyword);
	
	
	@Query(value="select u.first_name, u.last_name from Users u where u.first_name like %:keyword% or u.last_name like %:keyword%  ", nativeQuery = true)
	List<Object[]> findUserFirstNameAndLastNameByKeyword(@Param("keyword") String keyword);
	
	
	@Transactional
	@Modifying
	@Query(value="update users u set u.EMAIL_VERIFICATION_STATUS=:emailVerificationStatus where u.USER_ID=:userId", nativeQuery=true) 
	void updateUserEmailVerificationStatus(@Param("emailVerificationStatus") boolean emailVerificationStatus,@Param("userId") String userId);
	
	
	@Query("select user from UserEntity user where user.userId= :userId ")
	UserEntity findUserEntityByUserId(@Param("userId") String userId);
	
	@Query("select user.firstName, user.lastName from UserEntity user where user.userId= :userId ")
	List<Object[]> getUserEntityFullNameById(@Param("userId")String userId);
	
	@Modifying
	@Transactional	
	@Query("UPDATE UserEntity u SET u.emailVerificationStatus= :emailVerificationStatus WHERE u.userId= :userId")
	void updateUserEntityEmailVerificationStatus(
					@Param("emailVerificationStatus")boolean emailVerificationStatus,
					@Param("userId") String userId);
}
