package min.chat.child.schedule.repository;

import min.chat.child.schedule.entity.ScheduleCategory;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ScheduleCategoryRepository extends JpaRepository<ScheduleCategory, Long> {

    List<ScheduleCategory> findByAccountEmail(String email);

}