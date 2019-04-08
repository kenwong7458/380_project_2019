package ouhk.comps380f.controller;

import java.io.IOException;
import javax.annotation.Resource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import ouhk.comps380f.dao.ClassroomUserRepository;
import ouhk.comps380f.model.ClassroomUser;

@Controller
@RequestMapping("user")
public class ClassroomUserController {
    
    @Resource
    ClassroomUserRepository classroomUserRepo;
    
    @RequestMapping(value = {"", "list"}, method = RequestMethod.GET)
    public String list(ModelMap model) {
        model.addAttribute("classroomUsers", classroomUserRepo.findAll());
        return "listUser";
    }
    
    public static class CreateUserForm {
        
        private String username;
        private String email;
        private String password;
        private String cpassword;
        private String[] roles;

        public String getUsername() {
            return username;
        }

        public void setUsername(String username) {
            this.username = username;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }
        
        
        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getCpassword() {
            return cpassword;
        }

        public void setCpassword(String cpassword) {
            this.cpassword = cpassword;
        }
        
        public String[] getRoles() {
            return roles;
        }

        public void setRoles(String[] roles) {
            this.roles = roles;
        }
        
    }
    
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public ModelAndView create() {
        return new ModelAndView("addUser", "classroomUser", new CreateUserForm());
    }
    
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public View create(CreateUserForm form) throws IOException {
        if (form.getPassword().equals(form.getCpassword())) {
            ClassroomUser user = new ClassroomUser(form.getUsername(), 
                form.getPassword(), form.getEmail(), form.getRoles()
            );
            classroomUserRepo.save(user);
            return new RedirectView("/user/list", true);
        }
        else {
            return new RedirectView("/user/create", true);
        }
        
    }
    
    @RequestMapping(value = "delete/{username}", method = RequestMethod.GET)
    public View deleteUser(@PathVariable("username") String username) {
        classroomUserRepo.delete(classroomUserRepo.findOne(username));
        return new RedirectView("/user/list", true);
    }
}

