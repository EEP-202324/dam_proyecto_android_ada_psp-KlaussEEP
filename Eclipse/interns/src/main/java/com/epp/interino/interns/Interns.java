package com.epp.interino.interns;

import org.springframework.data.annotation.Id;

record Interns(@Id int id, String name, String surname, double amount, String boss) {
}
