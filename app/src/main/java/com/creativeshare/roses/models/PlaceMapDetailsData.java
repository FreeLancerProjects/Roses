package com.creativeshare.roses.models;

import java.io.Serializable;
import java.util.ArrayList;

public class PlaceMapDetailsData implements Serializable {
    public ArrayList<Candidates> getCandidates() {
        return candidates;
    }

    ArrayList<Candidates> candidates;

    public class Candidates implements Serializable {
        private String id;
        private String place_id;
        private String formatted_address;

        public String getFormatted_address() {
            return formatted_address;
        }

        public Geometry getGeometry() {
            return geometry;
        }

        private Geometry geometry;

        public class Geometry implements Serializable {
            public Location getLocation() {
                return location;
            }

            private Location location;

            public class Location {
                private double lat;

                public double getLat() {
                    return lat;
                }

                public double getLng() {
                    return lng;
                }

                private double lng;
            }

            public class viewport implements Serializable {
                public class Northeast implements Serializable {
                    private double lat;
                    private double lng;
                }

                public class Southwest {
                    private double lat;
                    private double lng;
                }
            }
        }
    }

}