package vincentcorp.vshop.Authenticator.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.vincent.inc.viesspringutils.controller.ViesController;

import vincentcorp.vshop.Authenticator.model.Role;
import vincentcorp.vshop.Authenticator.service.RoleService;

@RestController
@RequestMapping("/roles")
class RoleController extends ViesController<Role, Integer, RoleService> {

    public RoleController(RoleService service) {
        super(service);
    }
}