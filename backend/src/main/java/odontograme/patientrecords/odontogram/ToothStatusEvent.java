package odontograme.patientrecords.odontogram;

import java.time.Instant;

public class ToothStatusEvent {
    private Instant date;
    private int toothId;
    private Tooth.ToothStatus status;
    private Tooth.ToothFaceName face;
    private Boolean filled;
    private boolean planned;

    public ToothStatusEvent() {}

    public ToothStatusEvent(Instant date, int toothId, Tooth.ToothStatus status, boolean planned) {
        this.date = date;
        this.toothId = toothId;
        this.status = status;
        this.planned = planned;
    }

    public ToothStatusEvent(Instant date, int toothId, Tooth.ToothFaceName face, boolean filled, boolean planned) {
        this.date = date;
        this.toothId = toothId;
        this.face = face;
        this.filled = filled;
        this.planned = planned;
    }

    public Instant getDate() { return date; }
    public void setDate(Instant date) { this.date = date; }

    public int getToothId() { return toothId; }
    public void setToothId(int toothId) { this.toothId = toothId; }

    public Tooth.ToothStatus getStatus() { return status; }
    public void setStatus(Tooth.ToothStatus status) { this.status = status; }

    public Tooth.ToothFaceName getFace() { return face; }
    public void setFace(Tooth.ToothFaceName face) { this.face = face; }

    public Boolean getFilled() { return filled; }
    public void setFilled(Boolean filled) { this.filled = filled; }

    public boolean isPlanned() { return planned; }
    public void setPlanned(boolean planned) { this.planned = planned; }
}
