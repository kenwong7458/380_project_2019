package ouhk.comps380f.dao;

import org.springframework.data.jpa.repository.JpaRepository;
import ouhk.comps380f.model.Lecture;

public interface LectureRepository extends JpaRepository<Lecture, Long> {
    
}
