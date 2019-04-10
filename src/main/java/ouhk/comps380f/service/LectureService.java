package ouhk.comps380f.service;

import java.io.IOException;
import java.util.List;
import org.springframework.web.multipart.MultipartFile;
import ouhk.comps380f.exception.AttachmentNotFound;
import ouhk.comps380f.exception.LectureNotFound;
import ouhk.comps380f.model.Lecture;

public interface LectureService {
    public long createLecture(String lectureName, String tag,
        String description, List<MultipartFile> attachments) throws IOException;
    
    public List<Lecture> getLectures();
    
    public Lecture getLecture(long id);
    
    public void updateLecture(long id, String name, String tag,
        String description, List<MultipartFile> attachments)
        throws IOException, LectureNotFound;
    
    public void delete(long id) throws LectureNotFound;
    
    public void deleteAttachment(long lectureId, String name)
        throws AttachmentNotFound;
}
