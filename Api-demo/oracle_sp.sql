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


    public static void main(String[] args) {
        // SQL calling syntax using PL/SQL block matching the procedure signature
        String callStoredProc = "{call get_employee_details(?, ?)}";

        long inputEmployeeId = 100L; // Sample Input ID

        // Auto-closing resources using try-with-resources
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             CallableStatement stmt = conn.prepareCall(callStoredProc)) {

            System.out.println("Connected to Oracle Database successfully.");

// 1. Bind the IN parameter (Position 1)
            stmt.setLong(1, inputEmployeeId);

// 2. Register the OUT parameter as an Oracle Cursor type (Position 2)
            // OracleTypes.CURSOR value is typically -10
            stmt.registerOutParameter(2, OracleTypes.CURSOR);

            System.out.println("Executing stored procedure...");
            stmt.execute();

// 3. Extract the cursor result set from the OUT parameter
            try (ResultSet rs = (ResultSet) stmt.getObject(2)) {

                System.out.println("\n--- Query Results ---");
                boolean hasRows = false;

// 4. Iterate through the cursor rows
                while (rs.next()) {
                    hasRows = true;
                    long empId = rs.getLong("EMPLOYEE_ID");
                    String name = rs.getString("FIRST_NAME");
                    String email = rs.getString("EMAIL");
double salary = rs.getDouble("SALARY");

                    System.out.printf("ID: %d | Name: %s | Email: %s | Salary: $%.2f%n",
                                      empId, name, email, salary);
}

                if (!hasRows) {
                    System.out.println("No employee found with ID: " + inputEmployeeId);
}
            }

        } catch (SQLException e) {
            System.err.println("Database error occurred!");
            e.printStackTrace();
} catch (Exception e) {
            System.err.println("General error occurred!");
            e.printStackTrace();
}
    }