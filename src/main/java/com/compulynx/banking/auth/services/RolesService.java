package com.compulynx.banking.auth.services;
import com.compulynx.banking.auth.models.Role;
import com.compulynx.banking.auth.repositories.RolesRepository;
import com.compulynx.banking.utils.ResponseMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class RolesService {
    @Autowired
    private RolesRepository rolesRepository;

    public ResponseMessage addRole(Role role) {
        var res = new ResponseMessage();
        if (role != null && rolesRepository.findByName(role.getName()).isEmpty()) {
            rolesRepository.save(role);
            res.setMessage("Role Added Successfully!");
            res.setEntity(role);
            res.setStatus(true);
        } else {
            res.setMessage(role.getName() + " Already Exists!");
            res.setEntity(null);
            res.setStatus(false);
        }
        return res;
    }

    public List<Role> allRoles() {
        return rolesRepository.findAll();
    }

    public Role findRoleByName(String RoleName){
        if(rolesRepository.findByName(RoleName).isEmpty()){
            return null;
        }
        else {
            return rolesRepository.findByName(RoleName).get();
        }
    }
}
