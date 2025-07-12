package util;

import java.util.*;
import entity.*;

public class CourseScheduler {
    private static final int MAX_PERIODS = 10; // 每天最多10节课
    private static final int MAX_DAYS = 5;     // 每周5天
    private static final int MAX_WEEKS = 18;   // 最多18周

    public static Map<String, List<TimeSlot>> generateSchedule(List<Course> courses, List<Classroom> classrooms) {
        Map<String, List<TimeSlot>> scheduleMap = new HashMap<>();
        Map<String, Set<TimeSlot>> teacherOccupied = new HashMap<>();  // 教师已占用时间
        Map<String, Set<TimeSlot>> classOccupied = new HashMap<>();    // 班级已占用时间
        Map<String, Set<TimeSlot>> roomOccupied = new HashMap<>();     // 教室已占用时间
        
        // 按优先级对课程排序
        List<Course> sortedCourses = new ArrayList<>(courses);
        sortedCourses.sort((c1, c2) -> {
            // 优先考虑必修课和学分高的课程
            int creditCompare = Double.compare(c2.getCredits(), c1.getCredits());
            if (creditCompare != 0) return creditCompare;
            return Integer.compare(c2.getHours(), c1.getHours());
        });
        
        for (Course course : sortedCourses) {

            // 初始化各实体的已占用时间集合
            String teacherId = course.getTeacherId();
            String className = course.getClassName();
            teacherOccupied.putIfAbsent(teacherId, new HashSet<>());
            classOccupied.putIfAbsent(className, new HashSet<>());
            
            // 查找最佳时间段
            TimeSlot bestSlot = findBestTimeSlot(course,
                teacherOccupied.get(teacherId),
                classOccupied.get(className),
                roomOccupied);
                
            if (bestSlot != null) {
                // 分配最合适的教室
                String bestRoom = findBestRoom(bestSlot, classrooms, roomOccupied);
                if (bestRoom != null) {
                    // 更新课程信息
                    course.setTimeSlot(bestSlot.toString());
                    course.setClassroom(bestRoom);
                    
                    // 更新占用情况
                    teacherOccupied.get(teacherId).add(bestSlot);
                    classOccupied.get(className).add(bestSlot);
                    roomOccupied.computeIfAbsent(bestRoom, k -> new HashSet<>()).add(bestSlot);
                    
                    // 保存排课结果
                    scheduleMap.computeIfAbsent(course.getCourseId(), k -> new ArrayList<>())
                             .add(bestSlot);
                }
            }
        }
        
        return scheduleMap;
    }

    private static TimeSlot findBestTimeSlot(Course course,
                                           Set<TimeSlot> teacherSlots,
                                           Set<TimeSlot> classSlots,
                                           Map<String, Set<TimeSlot>> roomSlots) {
        List<TimeSlot> possibleSlots = new ArrayList<>();
        
        // 生成所有可能的时间段
        for (int day = 1; day <= MAX_DAYS; day++) {
            // 优先安排在上午和下午的黄金时段
            for (int period : Arrays.asList(1, 3, 5, 7, 9)) {
                TimeSlot slot = new TimeSlot(1, MAX_WEEKS, day, period, period + 1);
                if (isValidTimeSlot(slot, teacherSlots, classSlots, roomSlots)) {
                    possibleSlots.add(slot);
                }
            }
        }
        
        if (possibleSlots.isEmpty()) {
            return null;
        }
        
        // 根据时间段评分选择最佳时段
        possibleSlots.sort((s1, s2) -> Double.compare(
            evaluateTimeSlot(s2, course),
            evaluateTimeSlot(s1, course)
        ));
        
        return possibleSlots.get(0);
    }

    private static boolean isValidTimeSlot(TimeSlot slot,
                                         Set<TimeSlot> teacherSlots,
                                         Set<TimeSlot> classSlots,
                                         Map<String, Set<TimeSlot>> roomSlots) {
        // 检查教师和班级时间冲突
        if (teacherSlots.stream().anyMatch(ts -> ts.isConflict(slot)) ||
            classSlots.stream().anyMatch(ts -> ts.isConflict(slot))) {
            return false;
        }
        
        // 检查是否有可用教室
        return roomSlots.values().stream()
            .noneMatch(roomSlot -> roomSlot.stream()
                .anyMatch(ts -> ts.isConflict(slot)));
    }

    private static double evaluateTimeSlot(TimeSlot slot, Course course) {
        double score = 10.0;
        
        // 根据课程特点评估时间段适合度
        if (slot.getStartPeriod() <= 4) {  // 上午时段加分
            score += 2.0;
        } else if (slot.getStartPeriod() >= 9) {  // 晚上时段减分
            score -= 1.0;
        }
        
        // 避免课程安排在连续的午休时段
        if (slot.getStartPeriod() == 5) {
            score -= 0.5;
        }
        
        return score;
    }

    private static String findBestRoom(TimeSlot slot,
                                     List<Classroom> rooms,
                                     Map<String, Set<TimeSlot>> roomOccupied) {
        // 找出当前时间段未被占用的教室
        return rooms.stream()
            .map(Classroom::getRoomId)
            .filter(roomId -> {
                Set<TimeSlot> occupied = roomOccupied.get(roomId);
                return occupied == null || 
                       occupied.stream().noneMatch(ts -> ts.isConflict(slot));
            })
            .findFirst()
            .orElse(null);
    }

    public static List<String> validateSchedule(List<Course> courses) {
        List<String> conflicts = new ArrayList<>();
        for (int i = 0; i < courses.size(); i++) {
            Course c1 = courses.get(i);
            if (c1.getTimeSlot() == null) continue;
            
            TimeSlot ts1 = TimeSlot.fromString(c1.getTimeSlot());
            for (int j = i + 1; j < courses.size(); j++) {
                Course c2 = courses.get(j);
                if (c2.getTimeSlot() == null) continue;
                
                TimeSlot ts2 = TimeSlot.fromString(c2.getTimeSlot());
                if (hasConflict(c1, ts1, c2, ts2)) {
                    conflicts.add(String.format(
                        "冲突：%s(%s) 与 %s(%s) 在时间 %s",
                        c1.getName(), c1.getClassName(),
                        c2.getName(), c2.getClassName(),
                        c1.getTimeSlot()
                    ));
                }
            }
        }
        return conflicts;
    }

    private static boolean hasConflict(Course c1, TimeSlot ts1, Course c2, TimeSlot ts2) {
        if (!ts1.isConflict(ts2)) return false;
        
        return c1.getTeacherId().equals(c2.getTeacherId()) || // 教师冲突
               c1.getClassName().equals(c2.getClassName()) || // 班级冲突
               c1.getClassroom().equals(c2.getClassroom()); // 教室冲突
    }
}
