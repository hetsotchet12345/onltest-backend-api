package vn.onltest.repository;

import vn.onltest.model.entity.constant.ERole;
import vn.onltest.model.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(ERole name);

    Collection<Role> findRolesByNameIn(List<ERole> roleList);
}