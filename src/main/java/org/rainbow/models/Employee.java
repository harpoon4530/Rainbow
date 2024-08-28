//package org.rainbow.models;
//
//import com.google.common.base.Objects;
//
//public class Employee {
//    private String name;
//
//    private String supervisor;
//
//    public Employee(String name, String supervisor) {
//        this.name = name;
//        this.supervisor = supervisor;
//    }
//
//    public Employee(String name) {
//        this.name = name;
//        this.supervisor = null;
//    }
//
//    @Override
//    public boolean equals(Object o) {
//        if (this == o) return true;
//        if (o == null || getClass() != o.getClass()) return false;
//        Employee employee1 = (Employee) o;
//        return Objects.equal(name, employee1.name) && Objects.equal(supervisor, employee1.supervisor);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hashCode(name, supervisor);
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//
//    public void setSupervisor(String supervisor) {
//        this.supervisor = supervisor;
//    }
//
//    public String getSupervisor() {
//        return this.supervisor;
//    }
//}
