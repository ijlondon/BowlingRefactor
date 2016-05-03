/**
 * Created by iLondon on 4/29/16.
 * This is the state interface for the state pattern
 * which also contains the concrete state classes
 */


public interface FrameState {

    void setFrame(Frame frame);

    void addRoll(int pinsKOd, int rollNum);

    boolean canRollAgain();

    class Basic implements FrameState {
        Frame frame;
        boolean moreRolls;

        public void setFrame(Frame frame) {
            this.frame = frame;
            moreRolls = true;
        }

        public boolean canRollAgain() {
            return moreRolls;
        }

        @Override
        public void addRoll(int pinsKOd, int rollNum) {
            frame.pinCount += pinsKOd;
            frame.score = frame.pinCount;
            frame.rollNum = rollNum;

            //set number of pins knocked down by first roll
            if (rollNum == 1) {
                frame.roll1 = pinsKOd;
                //change frame's state to strike
                if (frame.pinCount == 10) frame.setFrameState(new Strike());
            }
            //set number of pins knocked down by second roll
            else if (rollNum == 2) {
                frame.roll2 = pinsKOd;
                //change frame's state to spare
                if (frame.pinCount == 10) frame.setFrameState(new Spare());
                moreRolls = false;
            }
        }
    }

    class Spare implements FrameState {
        Frame frame;
        boolean moreRolls;

        public void setFrame(Frame frame) {
            this.frame = frame;
            moreRolls = false;
        }

        public boolean canRollAgain() {
            return moreRolls;
        }

        @Override
        public void addRoll(int pinsKOd, int rollNum) {
            System.out.println("UH OH! addRoll called on " + frame.frameNum + "th frame in spare state");
        }
    }

    class Strike implements FrameState {
        Frame frame;
        boolean moreRolls;

        public void setFrame(Frame frame) {
            this.frame = frame;
            moreRolls = false;
        }

        public boolean canRollAgain() {
            return moreRolls;
        }

        @Override
        public void addRoll(int pinsKOd, int rollNum) {
            System.out.println("UH OH! addRoll called on " + frame.frameNum + "th frame in strike state");
        }
    }

    class Tenth implements FrameState {
        Frame frame;
        boolean moreRolls;

        public void setFrame(Frame frame) {
            this.frame = frame;
            moreRolls = true;
        }

        public boolean canRollAgain() {
            return moreRolls;
        }

        public void addRoll(int pinsKOd, int rollNum) {
            frame.pinCount += pinsKOd;
            frame.score = frame.pinCount;
            frame.rollNum = rollNum;

            if (rollNum == 1) {
                frame.roll1 = pinsKOd;
            }
            //set number of pins knocked down by second roll
            else if (rollNum == 2) {
                frame.roll2 = pinsKOd;
                //if spare or strike achieved then allow for third bonus roll
                if (frame.pinCount >= 10) {
                    moreRolls = true;
                } else {
                    moreRolls = false;
                }
            }
            //set number of pins knocked down by third roll
            else if (rollNum == 3) {
                frame.roll3 = pinsKOd;
                moreRolls = false;
            }
        }
    }
}