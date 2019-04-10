package ouhk.comps380f.controller;

import java.io.IOException;
import java.security.Principal;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.View;
import org.springframework.web.servlet.view.RedirectView;
import ouhk.comps380f.dao.ClassroomUserRepository;
import ouhk.comps380f.exception.AttachmentNotFound;
import ouhk.comps380f.exception.LectureNotFound;
import ouhk.comps380f.model.Attachment;
import ouhk.comps380f.model.ClassroomUser;
import ouhk.comps380f.model.Lecture;
import ouhk.comps380f.service.AttachmentService;
import ouhk.comps380f.service.LectureService;
import ouhk.comps380f.view.DownloadingView;

@Controller
@RequestMapping("classroom")
public class ClassroomController {
    
    @Autowired
    private LectureService lectureService;
    
    @Autowired
    private AttachmentService attachmentService;
    
    @Resource
    ClassroomUserRepository classroomUserRepo;
    
    private volatile long LECTURE_ID_SEQUENCE = 1;
    private Map<Long, Lecture> lectureDatabase = new Hashtable<>();
    
    @RequestMapping("/")
    public String index() {
        return "redirect:/classroom/listLecture";
    }
    
    @RequestMapping(value = "/login", method = RequestMethod.GET)
    public String login() {
        return "login";
    }
    @RequestMapping(value = {"", "listLecture"}, method = RequestMethod.GET)
    public String listLecture(ModelMap model) {
        model.addAttribute("lectureDatabase", lectureService.getLectures());
        return "listLecture";
    }
    
    @RequestMapping(value = "create", method = RequestMethod.GET)
    public ModelAndView create() {
        return new ModelAndView("addLecture", "lectureForm", new CreateLectureForm());
    }
    
    @RequestMapping(value = "view/{lectureId}", method = RequestMethod.GET)
    public String view(@PathVariable("lectureId") long lectureId, ModelMap model) {
        Lecture lecture = lectureService.getLecture(lectureId);
        if(lecture == null) {
            return "redirect:/classroom/listLecture";
        }
        model.addAttribute("lecture", lecture);
        return "view";
    }
    
    @RequestMapping(value = "view/{lectureId}/attachment/{attachment:.+}",
            method = RequestMethod.GET)
    public View download(@PathVariable("lectureId") long lectureId,
                         @PathVariable("attachment") String name) {
        Attachment attachment = attachmentService.getAttachment(lectureId, name);
        if (attachment != null) {
            return new DownloadingView(attachment.getName(),
            attachment.getMimeContentType(), attachment.getContents());
        }
        return new RedirectView("/classroom/listLecture", true);
    }
    
    @RequestMapping(value = "edit/{lectureId}", method = RequestMethod.GET)
    public ModelAndView showEdit(@PathVariable("lectureId") long lectureId,
            Principal principal, HttpServletRequest request) {
        Lecture lecture = lectureService.getLecture(lectureId);
        if (lecture == null
                || (!request.isUserInRole("ROLE_TEACHER"))) {
            return new ModelAndView(new RedirectView("/classroom/listLecture", true));
        }

        ModelAndView modelAndView = new ModelAndView("editLecture");
        modelAndView.addObject("lecture", lecture);

        CreateLectureForm lectureForm = new CreateLectureForm();
        lectureForm.setName(lecture.getName());
        lectureForm.setTag(lecture.getTag());
        lectureForm.setDescription(lecture.getDescription());
        modelAndView.addObject("lectureForm", lectureForm);

        return modelAndView;
    }
    
    @RequestMapping(value = "delete/{lectureId}", method = RequestMethod.GET)
    public String deleteLecture(@PathVariable("lectureId") long lectureId) 
        throws LectureNotFound {
        lectureService.delete(lectureId);
        return "redirect:/classroom/listLecture";
    }
    
    @RequestMapping(value = "/{lectureId}/delete/{attachment:.+}",method = RequestMethod.GET)
    public String deleteAttachment(@PathVariable("lectureId") long lectureId,
            @PathVariable("attachment") String name) throws AttachmentNotFound{
        lectureService.deleteAttachment(lectureId, name);
        return "redirect:/classroom/edit/" + lectureId;
    }
    
    @RequestMapping(value = "/registerAccount", method = RequestMethod.GET)
    public ModelAndView showRegisterForm() {
        return new ModelAndView("registerAccount", "registerAccountForm", new RegisterAccountForm());
    }
    
    public static class CreateLectureForm {
        private String name;
        private String tag;
        private String description;
        private List<MultipartFile> attachments;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getTag() {
            return tag;
        }

        public void setTag(String tag) {
            this.tag = tag;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public List<MultipartFile> getAttachments() {
            return attachments;
        }

        public void setAttachments(List<MultipartFile> attachments) {
            this.attachments = attachments;
        }

       
    }
    
    public static class RegisterAccountForm {
        private String username;
        private String email;
        private String password;
        private String cPassword;
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

        public String getcPassword() {
            return cPassword;
        }

        public void setcPassword(String cPassword) {
            this.cPassword = cPassword;
        }

        public String[] getRoles() {
            return roles;
        }

        public void setRoles(String[] roles) {
            this.roles = roles;
        }
        
    }
    
    @RequestMapping(value = "create", method = RequestMethod.POST)
    public String create(CreateLectureForm form, Principal principal) throws IOException {
        long lectureId = lectureService.createLecture(form.getName(),
                form.getTag(), form.getDescription(), form.getAttachments());
        return "redirect:/classroom/view/" + lectureId;
    }
    
    @RequestMapping(value = "registerAccount", method = RequestMethod.POST)
    public View registerAccount(RegisterAccountForm form) throws IOException {
        if (form.getPassword().equals(form.getcPassword())) {
            System.out.println("true");
            ClassroomUser user = new ClassroomUser(form.getUsername(), 
                form.getPassword(), form.getEmail(), form.getRoles()
            );
            classroomUserRepo.save(user);
            return new RedirectView("/login", true);
            //connect to database.
        }
        else {
            System.out.println("false");
            return new RedirectView("/classroom/registerAccount", true);
            //connect to database.
        }
    }
    
    @RequestMapping(value = "edit/{lectureId}", method = RequestMethod.POST)
    public View edit(@PathVariable("lectureId") long lectureId, CreateLectureForm form,
            Principal principal, HttpServletRequest request)
            throws IOException, LectureNotFound {
        Lecture lecture = lectureService.getLecture(lectureId);
        if (lecture == null
                || (!request.isUserInRole("ROLE_TEACHER"))) {
            return new RedirectView("/classroom/listLecture", true);
        }
        
        lectureService.updateLecture(lectureId, form.getName(), form.getTag(), form.getDescription(), form.getAttachments());
        return new RedirectView("/classroom/view/" + lectureId, true);
    }
    private synchronized long getNextLectureId() {
        return this.LECTURE_ID_SEQUENCE++;
    }
}
