/**
 * Created by iLondon on 4/29/16.
 * This is the state interface for the state pattern
 * which also contains the concrete state classes
 */


public interface FrameState {
    public enum FrameStatus {
        RESET_PINS, SUCCESS
    }
    void setFrame(Frame frame);

    FrameStatus addRoll(PinsetterEvent pe);

    int getScore();

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
        public FrameStatus addRoll(PinsetterEvent pe) {
            int pinsKOd = pe.pinsDownOnThisThrow();
            int rollNum = pe.getThrowNumber();
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
            return FrameStatus.SUCCESS;
        }


        @Override
        public int getScore(){
            frame.score = frame.pinCount;
            return frame.score;
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
        public FrameStatus addRoll(PinsetterEvent pe) {
            System.out.println("UH OH! addRoll called on " + frame.frameNum + "th frame in spare state");
            return FrameStatus.SUCCESS;
        }

        @Override
        public int getScore(){
            if(frame.nextFrame.roll1 != -1) {
                frame.score = frame.pinCount + frame.nextFrame.roll1;
            }
            return frame.score;
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
        public FrameStatus addRoll(PinsetterEvent pe) {
            System.out.println("UH OH! addRoll called on " + frame.frameNum + "th frame in strike state");
            return FrameStatus.SUCCESS;
        }

        @Override
        public int getScore(){
            if(frame.nextFrame.roll1 != -1 ) {
                frame.score = frame.pinCount + frame.nextFrame.roll1;
            }
                if(frame.nextFrame.state instanceof Strike){
                    if(frame.nextFrame.nextFrame.roll1 != -1 ) {
                        frame.score += frame.nextFrame.nextFrame.roll1;
                    }
                }
            return frame.score;
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

        public FrameStatus addRoll(PinsetterEvent pe) {
            int pinsKOd = pe.pinsDownOnThisThrow();
            int rollNum = pe.getThrowNumber();

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

            if (pe.totalPinsDown() == 10) {
                return FrameStatus.RESET_PINS;
            }
            return FrameStatus.SUCCESS;
        }

        @Override
        public int getScore(){
            return 0; //fill in
        }
    }
}