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

    public int[] getScore(int[] scores);

    boolean canRollAgain();


    class Basic implements FrameState {
        Frame frame;

        public void setFrame(Frame frame) {
            this.frame = frame;
        }

        public boolean canRollAgain() {
            return frame.nextFrame.roll1 == -1 || frame.nextFrame.canRollAgain();
        }

        @Override
        public FrameStatus addRoll(PinsetterEvent pe) {
            int pinsKOd = pe.pinsDownOnThisThrow();
            //set number of pins knocked down by first roll
            frame.roll1 = pinsKOd;
            frame.pinCount += pinsKOd;
            frame.score = frame.pinCount;
            frame.rollNum = pe.getThrowNumber();
            //change frame's state based on PE
            if (frame.pinCount == 10) {
                frame.setFrameState(new Strike());
            } else {
                frame.setFrameState(new NeedRoll());
            }
            return FrameStatus.SUCCESS;
        }


        @Override
        public int[] getScore(int[] scores){
            for (int i = frame.frameNum - 1; i < 10; i++) {
                scores[i] += frame.score;
            }
            return frame.nextFrame.getScore(scores);
        }


    }

    class NeedRoll implements FrameState {
        Frame frame;

        public void setFrame(Frame frame) {
            this.frame = frame;
        }

        public FrameStatus addRoll(PinsetterEvent pe) {
            int pinsKOd = pe.pinsDownOnThisThrow();
            frame.roll2 = pinsKOd;
            frame.pinCount += pinsKOd;
            frame.score += pe.pinsDownOnThisThrow();
            frame.rollNum = pe.getThrowNumber();
            //change frame's state to spare
            if (frame.pinCount == 10) {
                frame.setFrameState(new Spare());
            } else {
                frame.setFrameState(new  Pass());
            }
            return FrameStatus.SUCCESS;
        }

        public int[] getScore(int[] scores) {
            for (int i = frame.frameNum - 1; i < 10; i++) {
                scores[i] += frame.score;
            }
            return frame.nextFrame.getScore(scores);
        }

        public boolean canRollAgain() {
            return frame.nextFrame.roll1 == -1 || frame.nextFrame.canRollAgain();
        }
    }

    class Spare implements FrameState {
        Frame frame;

        public void setFrame(Frame frame) {
            this.frame = frame;
        }

        public boolean canRollAgain() {
            if (frame.nextFrame.roll1 == -1){
                return false;
            } else {
                return frame.nextFrame.canRollAgain();
            }
        }

        @Override
        public FrameStatus addRoll(PinsetterEvent pe) {
            frame.roll3 = pe.pinsDownOnThisThrow();
            frame.score += pe.pinsDownOnThisThrow();
            FrameState.FrameStatus returned = frame.nextFrame.addRoll(pe);
            frame.setFrameState(new Pass());
            return returned;
        }

        @Override
        public int[] getScore(int[] scores){
            for (int i = frame.frameNum - 1; i < 10; i++) {
                scores[i] += frame.score;
            }
            return frame.nextFrame.getScore(scores);
        }


    }

    class Strike implements FrameState {
        Frame frame;

        public void setFrame(Frame frame) {
            this.frame = frame;
        }

        public boolean canRollAgain() {
            if (frame.nextFrame.roll1 == -1){
                return false;
            } else {
                return frame.nextFrame.canRollAgain();
            }
        }

        @Override
        public FrameStatus addRoll(PinsetterEvent pe) {
            frame.roll2 = pe.pinsDownOnThisThrow();
            frame.score += frame.roll2;
            FrameState.FrameStatus returned = frame.nextFrame.addRoll(pe);
            frame.setFrameState(new Spare());
            return returned;
        }

        @Override
        public int[] getScore(int[] scores){
            for (int i = frame.frameNum - 1; i < 10; i++) {
                scores[i] += frame.score;
            }
            return frame.nextFrame.getScore(scores);
        }
    }

    class Pass implements FrameState {
        Frame frame;

        public void setFrame(Frame frame) {
            this.frame = frame;
        }

        public FrameStatus addRoll(PinsetterEvent pe) {
            return frame.nextFrame.addRoll(pe);
        }

        public int[] getScore(int[] scores) {
            for (int i = frame.frameNum - 1; i < 10; i++) {
                scores[i] += frame.score;
            }
            return frame.nextFrame.getScore(scores);
        }

        public boolean canRollAgain() {
            if(frame.nextFrame.roll1 == -1) {
                return false;
            } else {
                return frame.nextFrame.canRollAgain();
            }
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
            else if (rollNum == 2) {
                frame.roll2 = pinsKOd;
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
            if (pe.totalPinsDown() == 10 || pe.totalPinsDown() == 20) {
                return FrameStatus.RESET_PINS;
            }
            return FrameStatus.SUCCESS;
        }

        @Override
        public int[] getScore(int[] scores){
            for (int i = frame.frameNum - 1; i < 10; i++) {
                scores[i] += frame.score;
            }
            return scores;
        }
    }
}