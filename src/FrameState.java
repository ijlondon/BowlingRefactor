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
            //System.out.println("Basic current score(" + frame.frameNum + "): " + frame.score);
            //set number of pins knocked down by first roll
            if (frame.roll1 == -1 && pe.getThrowNumber() == 1) {
                frame.roll1 = pinsKOd;
                frame.pinCount += pinsKOd;
                frame.score = frame.pinCount;
                frame.rollNum = pe.getThrowNumber();
                //change frame's state to strike
                if (frame.pinCount == 10) {
                    frame.setFrameState(new Strike());
                }
            } else if (frame.roll2 == -1 && pe.getThrowNumber() == 2) {
                frame.roll2 = pinsKOd;
                frame.pinCount += pinsKOd;
                frame.score += pe.pinsDownOnThisThrow();
                frame.rollNum = pe.getThrowNumber();
                //change frame's state to spare
                if (frame.pinCount == 10) {
                    frame.setFrameState(new Spare());
                }
                moreRolls = false;
            } else {
                return frame.nextFrame.addRoll(pe);
            }
            return FrameStatus.SUCCESS;
        }


        @Override
        public int getScore(){
            return frame.score + frame.nextFrame.getScore();
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
            if (frame.roll3 == -1) {
                frame.roll3 = pe.pinsDownOnThisThrow();
                frame.score += pe.pinsDownOnThisThrow();
            }
            //System.out.println("Spare current score(" + frame.frameNum + "): " + frame.score);
            return frame.nextFrame.addRoll(pe);
        }

        @Override
        public int getScore(){
            return frame.score + frame.nextFrame.getScore();
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
            if (frame.roll2 == -1) {
                frame.roll2 = pe.pinsDownOnThisThrow();
                frame.score += frame.roll2;
            } else if (frame.roll3 == -1) {
                frame.roll3 = pe.pinsDownOnThisThrow();
                frame.score += frame.roll3;
            }
            //System.out.println("Strike current score(" + frame.frameNum + "): " + frame.score);
            return frame.nextFrame.addRoll(pe);
        }

        @Override
        public int getScore(){
            return frame.score + frame.nextFrame.getScore();
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
            boolean noAdd = true;
            if (rollNum == 1) {
                noAdd = false;
                frame.roll1 = pinsKOd;
            }
            else if (rollNum == 2) {
                frame.roll2 = pinsKOd;
                if (frame.pinCount == 10) {
                    noAdd = false;
                }
                if (frame.pinCount >= 10) {
                    moreRolls = true;
                } else {
                    moreRolls = false;
                    noAdd = false;
                }
            }
            //set number of pins knocked down by third roll
            else if (rollNum == 3) {
                frame.roll3 = pinsKOd;
                moreRolls = false;
            }
            if (noAdd && frame.pinCount > 10) {
                frame.score += pe.pinsDownOnThisThrow();
            }
            //System.out.println("Tenth current score(" + frame.frameNum + "): " + frame.score);
            if (pe.totalPinsDown() == 10 || pe.totalPinsDown() == 20) {
                return FrameStatus.RESET_PINS;
            }
            return FrameStatus.SUCCESS;
        }

        @Override
        public int getScore(){
            return frame.score;
        }
    }
}