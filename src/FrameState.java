/**
 * Created by iLondon on 4/29/16.
 * This is the state interface for the state pattern
 * which also contains the concrete state classes
 */


public interface FrameState {

    public void addScore(int addedScore);

    public class Inactive implements FrameState {

        @Override
        public void addScore(int addedScore) {

        }
    }

    public class Active implements FrameState {

        @Override
        public void addScore(int addedScore) {

        }
    }

    public class Spare implements FrameState {

        @Override
        public void addScore(int addedScore) {

        }
    }

    public class Strike implements FrameState {

        @Override
        public void addScore(int addedScore) {

        }
    }

    public class Tenth implements FrameState {

        @Override
        public void addScore(int addedScore) {

        }
    }
}