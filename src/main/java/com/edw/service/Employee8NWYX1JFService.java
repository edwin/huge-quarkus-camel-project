package com.edw.service;

import com.edw.soap.EmployeeServicePortType;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Named;
import org.apache.camel.component.cxf.jaxws.CxfEndpoint;
import org.eclipse.microprofile.config.inject.ConfigProperty;

@ApplicationScoped
public class Employee8NWYX1JFService {

    @ConfigProperty(name="employee.soap.url")
    private String employeeSoapUrl;

    @Named
    public CxfEndpoint employeeServiceEndpoint8NWYX1JF_getEmployeeById() {
        CxfEndpoint cxfEndpoint = new CxfEndpoint();
        cxfEndpoint.setServiceClass(EmployeeServicePortType.class);
        cxfEndpoint.setAddress(employeeSoapUrl);
        cxfEndpoint.setWsdlURL(null);
        cxfEndpoint.setDefaultOperationName("GetEmployeeById");
        cxfEndpoint.setDefaultOperationNamespace("http://localhost/employee");
        return cxfEndpoint;
    }

    @Named
    public CxfEndpoint employeeServiceEndpoint8NWYX1JF_getEmployeesByName() {
        CxfEndpoint cxfEndpoint = new CxfEndpoint();
        cxfEndpoint.setServiceClass(EmployeeServicePortType.class);
        cxfEndpoint.setAddress(employeeSoapUrl);
        cxfEndpoint.setWsdlURL(null);
        cxfEndpoint.setDefaultOperationName("GetEmployeesByName");
        cxfEndpoint.setDefaultOperationNamespace("http://localhost/employee");
        return cxfEndpoint;
    }
}