package com.vivi.cybernetics.util;

/**
 * Common easing functions to be used with {@link net.minecraft.util.Mth#lerp(float, float, float)}.
 * All formulas taken from <a href="https://easings.net">https://easings.net</a>
 */
public abstract class Easing {


    //in must be between 0 and 1!!!
    public abstract float ease(float x);


    public static final Easing LINEAR = new Easing() {
        @Override
        public float ease(float x) {
            return x;
        }
    };
    public static final Easing SINE_IN = new Easing() {
        @Override
        public float ease(float x) {
            return (float)(1 - Math.cos((x * Math.PI) / 2));
        }
    };
    public static final Easing SINE_OUT = new Easing() {
        @Override
        public float ease(float x) {
            return (float)Math.sin((x * Math.PI) / 2);
        }
    };
    public static final Easing SINE_IN_OUT = new Easing() {
        @Override
        public float ease(float x) {
            return (float)-(Math.cos(Math.PI * x) - 1) / 2;
        }
    };
    public static final Easing QUAD_IN = new Easing() {
        @Override
        public float ease(float x) {
            return x*x;
        }
    };
    public static final Easing QUAD_OUT = new Easing() {
        @Override
        public float ease(float x) {
            return 1 - (1 - x) * (1 - x);
        }
    };
    public static final Easing QUAD_IN_OUT = new Easing() {
        @Override
        public float ease(float x) {
            return (float)(x < 0.5 ? 2 * x * x : 1 - Math.pow(-2 * x + 2, 2) / 2);
        }
    };
    public static final Easing CUBIC_IN = new Easing() {
        @Override
        public float ease(float x) {
            return x*x*x;
        }
    };
    public static final Easing CUBIC_OUT = new Easing() {
        @Override
        public float ease(float x) {
            return (float)(1 - Math.pow(1 - x, 3));
        }
    };
    public static final Easing CUBIC_IN_OUT = new Easing() {
        @Override
        public float ease(float x) {
            return (float)(x < 0.5 ? 4 * x * x * x : 1 - Math.pow(-2 * x + 2, 3) / 2);
        }
    };
    public static final Easing QUART_IN = new Easing() {
        @Override
        public float ease(float x) {
            return x*x*x*x;
        }
    };
    public static final Easing QUART_OUT = new Easing() {
        @Override
        public float ease(float x) {
            return (float)(1 - Math.pow(1 - x, 4));
        }
    };
    public static final Easing QUART_IN_OUT = new Easing() {
        @Override
        public float ease(float x) {
            return (float)(x < 0.5 ? 8 * x * x * x * x : 1 - Math.pow(-2 * x + 2, 4) / 2);
        }
    };
    public static final Easing QUINT_IN = new Easing() {
        @Override
        public float ease(float x) {
            return x*x*x*x*x;
        }
    };
    public static final Easing QUINT_OUT = new Easing() {
        @Override
        public float ease(float x) {
            return (float)(1 - Math.pow(1 - x, 5));
        }
    };
    public static final Easing QUINT_IN_OUT = new Easing() {
        @Override
        public float ease(float x) {
            return (float)(x < 0.5 ? 16 * x * x * x * x * x : 1 - Math.pow(-2 * x + 2, 5) / 2);
        }
    };
    public static final Easing EXPO_IN = new Easing() {
        @Override
        public float ease(float x) {
            return (float)(x == 0 ? 0 : Math.pow(2, 10 * x - 10));
        }
    };
    public static final Easing EXPO_OUT = new Easing() {
        @Override
        public float ease(float x) {
            return (float)(x == 1 ? 1 : 1 - Math.pow(2, -10 * x));
        }
    };
    public static final Easing EXPO_IN_OUT = new Easing() {
        @Override
        public float ease(float x) {
            return (float)(x == 0
                    ? 0
                    : x == 1
                    ? 1
                    : x < 0.5 ? Math.pow(2, 20 * x - 10) / 2
                    : (2 - Math.pow(2, -20 * x + 10)) / 2);
        }
    };
    public static final Easing CIRC_IN = new Easing() {
        @Override
        public float ease(float x) {
            return (float)(1 - Math.sqrt(1 - Math.pow(x, 2)));
        }
    };
    public static final Easing CIRC_OUT = new Easing() {
        @Override
        public float ease(float x) {
            return (float)(Math.sqrt(1 - Math.pow(x - 1, 2)));
        }
    };
    public static final Easing CIRC_IN_OUT = new Easing() {
        @Override
        public float ease(float x) {
            return (float)(x < 0.5
                    ? (1 - Math.sqrt(1 - Math.pow(2 * x, 2))) / 2
                    : (Math.sqrt(1 - Math.pow(-2 * x + 2, 2)) + 1) / 2);
        }
    };


    public static final Easing BACK_IN = new Easing() {
        @Override
        public float ease(float x) {
            float c1 = 1.70158f;
            float c3 = c1 + 1;
            return (c3 * x * x * x - c1 * x * x);
        }
    };
    public static final Easing BACK_OUT = new Easing() {
        @Override
        public float ease(float x) {
            float c1 = 1.70158f;
            float c3 = c1 + 1;
            return (float)(1 + c3 * Math.pow(x - 1, 3) + c1 * Math.pow(x - 1, 2));
        }
    };
    public static final Easing BACK_IN_OUT = new Easing() {
        @Override
        public float ease(float x) {
            float c1 = 1.70158f;
            float c2 = c1 * 1.525f;
            return (float)(x < 0.5
                    ? (Math.pow(2 * x, 2) * ((c2 + 1) * 2 * x - c2)) / 2
                    : (Math.pow(2 * x - 2, 2) * ((c2 + 1) * (x * 2 - 2) + c2) + 2) / 2);
        }
    };


    public static final Easing ELASTIC_IN = new Easing() {
        @Override
        public float ease(float x) {
            float c4 = (float)(2 * Math.PI) / 3;

            return (float)(x == 0
                    ? 0
                    : x == 1
                    ? 1
                    : -Math.pow(2, 10 * x - 10) * Math.sin((x * 10 - 10.75) * c4));
        }
    };
    public static final Easing ELASTIC_OUT = new Easing() {
        @Override
        public float ease(float x) {
            float c4 = (float)(2 * Math.PI) / 3;

            return (float)(x == 0
                    ? 0
                    : x == 1
                    ? 1
                    : Math.pow(2, -10 * x) * Math.sin((x * 10 - 0.75) * c4) + 1);
        }
    };
    public static final Easing ELASTIC_IN_OUT = new Easing() {
        @Override
        public float ease(float x) {
            float c5 = (float)(2 * Math.PI) / 4.5f;

            return (float)(x == 0
                    ? 0
                    : x == 1
                    ? 1
                    : x < 0.5
                    ? -(Math.pow(2, 20 * x - 10) * Math.sin((20 * x - 11.125) * c5)) / 2
                    : (Math.pow(2, -20 * x + 10) * Math.sin((20 * x - 11.125) * c5)) / 2 + 1);
        }
    };


    public static final Easing BOUNCE_IN = new Easing() {
        @Override
        public float ease(float x) {
            return 1 - bounceOut(1 - x);
        }

        private float bounceOut(float x) {
            float n1 = 7.5625f;
            float d1 = 2.75f;

            if (x < 1 / d1) {
                return n1 * x * x;
            } else if (x < 2 / d1) {
                return n1 * (x -= 1.5f / d1) * x + 0.75f;
            } else if (x < 2.5 / d1) {
                return n1 * (x -= 2.25f / d1) * x + 0.9375f;
            } else {
                return n1 * (x -= 2.625f / d1) * x + 0.984375f;
            }
        }
    };
    public static final Easing BOUNCE_OUT = new Easing() {
        @Override
        public float ease(float x) {
            float n1 = 7.5625f;
            float d1 = 2.75f;

            if (x < 1 / d1) {
                return n1 * x * x;
            } else if (x < 2 / d1) {
                return n1 * (x -= 1.5f / d1) * x + 0.75f;
            } else if (x < 2.5 / d1) {
                return n1 * (x -= 2.25f / d1) * x + 0.9375f;
            } else {
                return n1 * (x -= 2.625f / d1) * x + 0.984375f;
            }
        }
    };
    public static final Easing BOUNCE_IN_OUT = new Easing() {
        @Override
        public float ease(float x) {
            return x < 0.5
                    ? (1 - bounceOut(1 - 2 * x)) / 2
                    : (1 + bounceOut(2 * x - 1)) / 2;
        }

        private float bounceOut(float x) {
            float n1 = 7.5625f;
            float d1 = 2.75f;

            if (x < 1 / d1) {
                return n1 * x * x;
            } else if (x < 2 / d1) {
                return n1 * (x -= 1.5f / d1) * x + 0.75f;
            } else if (x < 2.5 / d1) {
                return n1 * (x -= 2.25f / d1) * x + 0.9375f;
            } else {
                return n1 * (x -= 2.625f / d1) * x + 0.984375f;
            }
        }
    };



}
