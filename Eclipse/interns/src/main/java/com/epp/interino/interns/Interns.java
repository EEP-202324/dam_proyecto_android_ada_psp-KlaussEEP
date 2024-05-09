package com.epp.interino.interns;

import org.springframework.data.annotation.Id;

record Interns(@Id Integer id, String name, String surname, double amount, String boss) {
}
