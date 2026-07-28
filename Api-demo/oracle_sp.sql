CREATE OR REPLACE PROCEDURE get_employee_details (
    p_emp_id     IN  NUMBER,
    p_recordset  OUT SYS_REFCURSOR
) AS
BEGIN
OPEN p_recordset FOR
SELECT employee_id, first_name, email, salary
FROM employees
WHERE employee_id = p_emp_id;
END;

public List<Employee> getEmployeeById(Long empId) {
        // Define a row mapper to translate Oracle rows into your Java object
        RowMapper<Employee> employeeRowMapper = (rs, rowNum) -> {
            Employee emp = new Employee();
            emp.setId(rs.getLong("EMPLOYEE_ID"));
            emp.setName(rs.getString("FIRST_NAME"));
            emp.setEmail(rs.getString("EMAIL"));
            emp.setSalary(rs.getDouble("SALARY"));
            return emp;
        };

        // Initialize SimpleJdbcCall with procedure details
        SimpleJdbcCall jdbcCall = new SimpleJdbcCall(jdbcTemplate)
                .withProcedureName("get_employee_details")
                .returningResultSet("p_recordset", employeeRowMapper); // Links OUT parameter to RowMapper

        // Bind the input parameters
        SqlParameterSource inputParams = new MapSqlParameterSource()
                .addValue("p_emp_id", empId);

        // Execute and retrieve the mapped result list from the map
        Map<String, Object> output = jdbcCall.execute(inputParams);

        // Oracle maps the specified OUT parameter key as a List
        return (List<Employee>) output.get("p_recordset");
}