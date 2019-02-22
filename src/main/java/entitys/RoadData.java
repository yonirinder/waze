package entitys;


public class RoadData {

    private int time;
    private float length;


    public RoadData(String time, String length) {
        this.time = convertTime(time);
        this.length = convertLength(length);
    }

    private float convertLength(String length) {
        return Float.valueOf(length.split(" ")[0]);
    }

    private int convertTime(String time) {
        if (!time.contains("h"))
            return Integer.parseInt(time.split(" ")[0]);
        else {
            int result = 0;
            String[] list = time.split(" ");
            result += Integer.parseInt(list[0]) * 60;
            if (list.length > 2)
                result += Integer.parseInt(list[2]);
            return result;
        }
    }

    public int getTime() {
        return time;
    }

    public float getLength() {
        return length;
    }

    @Override
    public String toString() {
        return "{" +
                "time='" + time + '\'' +
                ", length='" + length + '\'' +
                '}';
    }

}

