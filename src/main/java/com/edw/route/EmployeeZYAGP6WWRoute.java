package com.edw.route;

import com.edw.soap.EmployeeByIdRequest;
import com.edw.soap.EmployeeByNameRequest;
import com.edw.soap.EmployeeResponse;
import com.edw.soap.EmployeesResponse;
import jakarta.enterprise.context.ApplicationScoped;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.cxf.message.MessageContentsList;

import java.util.HashMap;

@ApplicationScoped
public class EmployeeZYAGP6WWRoute extends RouteBuilder {
    @Override
    public void configure() throws Exception {
        // default exception
        onException(Exception.class).handled(true)
                .log("something wrong with SOAP endpoint")
                .process(exchange -> {
                    // clear
                    exchange.getIn().getHeaders().clear();

                    // send msg
                    final Throwable t = exchange.getProperty(Exchange.EXCEPTION_CAUGHT, Throwable.class);
                    exchange.getMessage().setBody(new HashMap(){{
                        put("error", "exception happen");
                        put("reason", t.getMessage());
                    }});
                })
                .setHeader(Exchange.HTTP_RESPONSE_CODE, constant(500))
                .marshal()
                .json(JsonLibrary.Jackson)
                .end();

        rest("/api")
            .get("/employees_ZYAGP6WW/{id}")
                .produces("application/json")
                    .type(EmployeeResponse.class)
                        .to("direct:get-employee_ZYAGP6WW")
            .get("/employees_ZYAGP6WW/{firstname}/{lastname}")
                .produces("application/json")
                    .type(EmployeesResponse.class)
                        .to("direct:get-employees-by-firstname-lastname_ZYAGP6WW");
        // get one employee
        from("direct:get-employee_ZYAGP6WW")
                .routeId("get-employee-api_ZYAGP6WW")
                .log("calling get-employee to wsdl")
                .process(exchange -> {
                    // get the employee-id from path variable
                    String id = (String) exchange.getIn().getHeader("id");
                    exchange.getIn().getHeaders().clear();

                    // set the employee-id to wsdl request
                    EmployeeByIdRequest employeeByIdRequest = new EmployeeByIdRequest();
                    employeeByIdRequest.setId(Long.parseLong(id));
                    exchange.getOut().setBody(employeeByIdRequest);
                })
                // soap request
                .to("cxf:bean:employeeServiceEndpointZYAGP6WW_getEmployeeById")
                // process the response
                .process(exchange -> {
                    exchange.getIn().getHeaders().clear();
                    EmployeeResponse employeeResponse = (EmployeeResponse) ((MessageContentsList) exchange.getIn().getBody()).get(0);
                    exchange.getOut().setBody(employeeResponse);
                })
                .log("sending response get-employee to frontend as json")
                .marshal()
                .json(JsonLibrary.Jackson);

        // get employees based on first and lastname
        from("direct:get-employees-by-firstname-lastname_ZYAGP6WW")
                .routeId("get-employees-by-firstname-lastname-api_ZYAGP6WW")
                .log("calling get-employees-by-firstname-lastname to wsdl")
                .process(exchange -> {
                    // get the employee firstname and lastname from path variable
                    String firstname = (String) exchange.getIn().getHeader("firstname");
                    String lastname = (String) exchange.getIn().getHeader("lastname");
                    exchange.getIn().getHeaders().clear();

                    // set the employee names to wsdl request
                    EmployeeByNameRequest employeeByNameRequest = new EmployeeByNameRequest();
                    employeeByNameRequest.setFirstname(firstname);
                    employeeByNameRequest.setLastname(lastname);
                    exchange.getOut().setBody(employeeByNameRequest);
                })
                // soap request
                .to("cxf:bean:employeeServiceEndpointZYAGP6WW_getEmployeesByName")
                // process the response
                .process(exchange -> {
                    exchange.getIn().getHeaders().clear();
                    EmployeesResponse employeesResponse = (EmployeesResponse) ((MessageContentsList) exchange.getIn().getBody()).get(0);
                    exchange.getOut().setBody(employeesResponse);
                })
                .log("sending response get-employees-by-firstname-lastname to frontend as json")
                .marshal()
                .json(JsonLibrary.Jackson);
    }
}