/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import java.util.Comparator;
import model.Link;

/**
 *
 * @author Znoque
 */
public class TituloComparator implements Comparator<Link>{

    @Override
    public int compare(Link l1, Link l2) {
        if (l1.getTitulo().get().equals(l2.getTitulo().get())) {
            return 0;
        }
        if (l1.getTitulo().get().compareTo(l2.getTitulo().get()) > 0) {
            return 1;
        }
        return -1;
    }
    
}
