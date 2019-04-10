package ouhk.comps380f.service;

import java.io.IOException;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import ouhk.comps380f.dao.AttachmentRepository;
import ouhk.comps380f.dao.LectureRepository;
import ouhk.comps380f.exception.AttachmentNotFound;
import ouhk.comps380f.exception.LectureNotFound;
import ouhk.comps380f.model.Attachment;
import ouhk.comps380f.model.Lecture;

@Service
public class LectureServiceImpl implements LectureService {
    
    @Resource
    private LectureRepository lectureRepo;
    
    @Resource
    private AttachmentRepository attachmentRepo;
    
    @Override
    @Transactional
    public List<Lecture> getLectures() {
        return lectureRepo.findAll();
    }
    
    @Override
    @Transactional
    public Lecture getLecture(long id) {
        return lectureRepo.findOne(id);
    }
    
    @Override
    @Transactional(rollbackFor = LectureNotFound.class)
    public void delete(long id) throws LectureNotFound {
        Lecture deletedLecture = lectureRepo.findOne(id);
        if (deletedLecture == null) {
            throw new LectureNotFound();
        }
        lectureRepo.delete(deletedLecture);
    }
    
    @Override
    @Transactional(rollbackFor = AttachmentNotFound.class)
    public void deleteAttachment(long lectureId, String name) throws AttachmentNotFound {
        Lecture lecture = lectureRepo.findOne(lectureId);
        for (Attachment attachment : lecture.getAttachments()) {
            if (attachment.getName().equals(name)) {
                lecture.deleteAttachment(attachment);
                lectureRepo.save(lecture);
                return;
            }
        }
        throw new AttachmentNotFound();
    }
    
    @Override
    @Transactional
    public long createLecture(String name, String tag,
        String description, List<MultipartFile> attachments) throws IOException {
    Lecture lecture = new Lecture();
    lecture.setName(name);
    lecture.setTag(tag);
    lecture.setDescription(description);
    
    for (MultipartFile filePart : attachments) {
        Attachment attachment = new Attachment();
        attachment.setName(filePart.getOriginalFilename());
        attachment.setMimeContentType(filePart.getContentType());
        attachment.setContents(filePart.getBytes());
        attachment.setLecture(lecture);
        if (attachment.getName() != null && attachment.getName().length() > 0
                && attachment.getContents() != null
                && attachment.getContents().length > 0) {
            lecture.getAttachments().add(attachment);
            }
        }
        Lecture savedLecture = lectureRepo.save(lecture);
        return savedLecture.getId();
    }
    
    @Override
    @Transactional(rollbackFor = LectureNotFound.class)
    public void updateLecture(long id, String name, String tag,
            String description, List<MultipartFile> attachments)
            throws IOException, LectureNotFound {
        Lecture updatedLecture = lectureRepo.findOne(id);
        if (updatedLecture == null) {
            throw new LectureNotFound();
        }
        
        updatedLecture.setName(name);
        updatedLecture.setTag(tag);
        updatedLecture.setDescription(description);
        
    for (MultipartFile filePart : attachments) {
        Attachment attachment = new Attachment();
        attachment.setName(filePart.getOriginalFilename());
        attachment.setMimeContentType(filePart.getContentType());
        attachment.setContents(filePart.getBytes());
        attachment.setLecture(updatedLecture);
        if (attachment.getName() != null && attachment.getName().length() > 0
                && attachment.getContents() != null
                && attachment.getContents().length > 0) {
            updatedLecture.getAttachments().add(attachment);
        }
    }
    lectureRepo.save(updatedLecture);
    }
}
