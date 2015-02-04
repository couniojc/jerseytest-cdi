/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.test.simplerest;

import javax.enterprise.context.ApplicationScoped;

/**
 *
 * @author jcounio
 */
@ApplicationScoped
public class MyInj {
    
   String getString() {
       return "aaa";
   } 
}
