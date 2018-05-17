package game.controller;

public class UnionFind {

        private int[] id;    // id[i] = component identifier of i
        private int[] types; //type of food; 0 - undefined, 1 - communication, 2 - cooperation
        private int count;   // number of components
        private int n=13; //max possible chain length

        /**
         * Initializes an empty union–find data structure with {@code n} sites
         * {@code 0} through {@code n-1}. Each site is initially in its own
         * component.
         */
        public UnionFind() {
            count = n;
            id = new int[n];
            types=new int[n];
            for (int i = 0; i < n; i++)
                id[i] = i;
        }

        /**
         * Returns the number of components.
         *
         * @return the number of components (between {@code 1} and {@code n})
         */
        public int count() {
            return count;
        }

        /**
         * Returns the component identifier for the component containing site {@code p}.
         *
         * @param  p the integer representing one site
         * @return the component identifier for the component containing site {@code p}
         * @throws IllegalArgumentException unless {@code 0 <= p < n}
         */
        public int find(int p) {
            validate(p);
            return id[p];
        }

        // validate that p is a valid index
        private void validate(int p) {
            int n = id.length;
            if (p < 0 || p >= n) {
                throw new IllegalArgumentException("index " + p + " is not between 0 and " + (n-1));
            }
        }

        /**
         * Returns true if the the two sites are in the same component.
         */
        public boolean connected(int p, int q) {
            validate(p);
            validate(q);
            return id[p] == id[q];
        }

        /**
         * Merges the component containing site {@code p} with the
         * the component containing site {@code q}.
         */
        public void union(int p, int q, String t) {
            validate(p);
            validate(q);
            int pID = id[p];   // needed for correctness
            int qID = id[q];   // to reduce the number of array accesses

            if (pID == qID) return;

            for (int i = 0; i < id.length; i++)
                if (id[i] == pID) id[i] = qID;
            count--;

            int type=0;
            if (t.equals("Cooperation")) type=2;
            if (t.equals("Communication")) type=1;

            if (type>types[p]) types[p]=type; //upgrade communication or undefined,but not cooperation
            if (type>types[q]) types[q]=type;
        }


}