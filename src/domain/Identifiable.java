package domain;

import java.io.Serializable;


public interface Identifiable<ID> extends Serializable {
    ID getID();
}