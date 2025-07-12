package util;

public class TimeSlot {
    private int startWeek;
    private int endWeek;
    private int dayOfWeek;
    private int startPeriod;
    private int endPeriod;

    public TimeSlot(int startWeek, int endWeek, int dayOfWeek, int startPeriod, int endPeriod) {
        this.startWeek = startWeek;
        this.endWeek = endWeek;
        this.dayOfWeek = dayOfWeek;
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
    }

    public boolean isConflict(TimeSlot other) {
        // 判断两个时间段是否冲突
        if (dayOfWeek != other.dayOfWeek) return false;
        if (endWeek < other.startWeek || startWeek > other.endWeek) return false;
        if (endPeriod < other.startPeriod || startPeriod > other.endPeriod) return false;
        return true;
    }

    @Override
    public String toString() {
        return String.format("%d-%d周,周%d,%d-%d节", 
            startWeek, endWeek, dayOfWeek, startPeriod, endPeriod);
    }

    public static TimeSlot fromString(String timeSlotStr) {
        if (timeSlotStr == null || timeSlotStr.trim().isEmpty()) {
            return null;
        }
        
        try {
            String[] parts = timeSlotStr.split(";");
            if (parts.length < 3) {
                return null;
            }

            // 解析周数
            String weekPart = parts[0].replace("周", "");
            int startWeek, endWeek;
            if (weekPart.contains("-")) {
                String[] weeks = weekPart.split("-");
                startWeek = Integer.parseInt(weeks[0]);
                endWeek = Integer.parseInt(weeks[1]);
            } else {
                startWeek = endWeek = Integer.parseInt(weekPart);
            }

            // 解析星期
            int dayOfWeek = Integer.parseInt(parts[1].replace("周", ""));

            // 解析节数
            String periodPart = parts[2].replace("节", "");
            String[] periods = periodPart.split("-");
            int startPeriod = Integer.parseInt(periods[0]);
            int endPeriod = Integer.parseInt(periods[1]);

            return new TimeSlot(startWeek, endWeek, dayOfWeek, startPeriod, endPeriod);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    // Getters and setters
    public int getStartWeek() { return startWeek; }
    public void setStartWeek(int startWeek) { this.startWeek = startWeek; }
    public int getEndWeek() { return endWeek; }
    public void setEndWeek(int endWeek) { this.endWeek = endWeek; }
    public int getDayOfWeek() { return dayOfWeek; }
    public void setDayOfWeek(int dayOfWeek) { this.dayOfWeek = dayOfWeek; }
    public int getStartPeriod() { return startPeriod; }
    public void setStartPeriod(int startPeriod) { this.startPeriod = startPeriod; }
    public int getEndPeriod() { return endPeriod; }
    public void setEndPeriod(int endPeriod) { this.endPeriod = endPeriod; }
}
