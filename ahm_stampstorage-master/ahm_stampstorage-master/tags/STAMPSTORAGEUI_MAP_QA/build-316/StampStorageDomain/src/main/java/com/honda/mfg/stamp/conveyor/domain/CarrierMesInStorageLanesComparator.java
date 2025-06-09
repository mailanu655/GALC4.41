package com.honda.mfg.stamp.conveyor.domain;

import java.util.Comparator;

/**
 * User: Jeffrey M Lutz
 * Date: 6/19/11
 */
public class CarrierMesInStorageLanesComparator implements Comparator<CarrierMes> {

    @Override
    public int compare(CarrierMes lhs, CarrierMes rhs) {
        if (lhs.getDestination() < rhs.getDestination()) {
            return -1;
        } else if (lhs.getDestination() > rhs.getDestination()) {
            return 1;
        }
        // Destinations are equal
        Integer lhsBuffer = lhs.getBuffer();
        Integer rhsBuffer = rhs.getBuffer();

        if(lhsBuffer == null) {
            if(rhsBuffer != null) {
                return -1;
            }
        } else {
            if(rhsBuffer == null) {
                return 1;
            }
            if(lhsBuffer < rhsBuffer ) {
                return 1;
            } else if (lhsBuffer > rhsBuffer) {
                return -1;
            }
        }

//        if(lhs.getBuffer() == null ) {
//            if(rhs.getBuffer() != null) {
//                return 1;
//            }
//        }
//        if(lhs.getBuffer() != null ) {
//            if(rhs.getBuffer() == null) {
//                return -1;
//            }
//        }
        // Destinations are equal and buffers are equals
        if (lhs.getUpdateDate().before(rhs.getUpdateDate())) {
            return -1;
        } else if (lhs.getUpdateDate().after(rhs.getUpdateDate())) {
            return 1;
        }
        return 0;
    }
}
