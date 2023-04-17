
import java.sql.*;

public class DatabaseManipulation implements DataManipulation {
    private Connection con = null;//数据库访问的链接，一定要先创建链接
    private ResultSet resultSet;//结果集，尤其对搜索语句，会放到这里

    private String host = "localhost";
    private String dbname = "cs307";
    private String user = "checker";
    private String pwd = "123456";
    private String port = "5432";

//创建链接
    @Override
    public void openDatasource() {
        try {
            Class.forName("org.postgresql.Driver");

        } catch (Exception e) {
            System.err.println("Cannot find the PostgreSQL driver. Check CLASSPATH.");
            System.exit(1);
        }

        try {
            String url = "jdbc:postgresql://" + host + ":" + port + "/" + dbname;
            con = DriverManager.getConnection(url, user, pwd);//用驱动和必要的参数创建链接

        } catch (SQLException e) {
            System.err.println("Database connection failed");
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }


    //关闭数据库链接
    @Override
    public void closeDatasource() {
        if (con != null) {
            try {
                con.close();
                con = null;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int addOneMovie(String str) {
        int result = 0;
        String sql = "insert into movies (title, country,year_released,runtime) " +
                "values (?,?,?,?)";
        String[] movieInfo = str.split(";");
        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setString(1, movieInfo[0]);
            preparedStatement.setString(2, movieInfo[1]);
            preparedStatement.setInt(3, Integer.parseInt(movieInfo[2]));
            preparedStatement.setInt(4, Integer.parseInt(movieInfo[3]));
            System.out.println(preparedStatement.toString());

            result = preparedStatement.executeUpdate();

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return result;
    }

    @Override
    public String allContinentNames() {
        StringBuilder sb = new StringBuilder();//类似编译sql语句
        String sql = "select continent from countries group by continent";
        try {
            Statement statement = con.createStatement();
            resultSet = statement.executeQuery(sql);//执行sql语句并有返回值
            //resultset类似游标，指向返回结果集的头位置
            //当有下一个
            while (resultSet.next()) {
//                resultSet.getString("") //这样可以返回行，列之类的
                sb.append(resultSet.getString("continent")).append("\n");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    @Override
    public String continentsWithCountryCount() {
        StringBuilder sb = new StringBuilder();
        String sql = "select continent, count(*) countryNumber from countries group by continent;";
        try {
            Statement statement = con.createStatement();
            resultSet = statement.executeQuery(sql);
            while (resultSet.next()) {
                sb.append(resultSet.getString("continent")).append("\t");
                sb.append(resultSet.getString("countryNumber"));
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return sb.toString();
    }

    @Override
    public String FullInformationOfMoviesRuntime(int min, int max) {
        StringBuilder sb = new StringBuilder();
        String sql = "select m.title,c.country_name country,c.continent ,m.runtime " +
                "from movies m " +
                "join countries c on m.country=c.country_code " +
                "where m.runtime between ? and ? order by runtime;";
        try {
            //有参数传进来时用这个
            //statement是将sql发送到dbms后，编译与执行
            //preparedstatement 是预编译，执行的时候，将？位置转换成对应参数执行
            //可以防止sql注入，防止传入参数包含' 而报错，对批量操作，加快效率，创建开销比statement大一点
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, min);
            preparedStatement.setInt(2, max);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                sb.append(resultSet.getString("runtime")).append("\t");
                sb.append(String.format("%-18s", resultSet.getString("country")));
                sb.append(resultSet.getString("continent")).append("\t");
                sb.append(resultSet.getString("title")).append("\t");
                sb.append(System.lineSeparator());
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    @Override
    public String findMovieById(int id) {
        StringBuilder sb = new StringBuilder();
        String sql = "select m.title, c.country_name, m.year_released, m.runtime from movies m " +
                "  join countries c " +
                "      on m.country = c.country_code " +
                "where m.movieid = ?;";

        try {
            PreparedStatement preparedStatement = con.prepareStatement(sql);
            preparedStatement.setInt(1, id);
            resultSet = preparedStatement.executeQuery();
            resultSet.next();
//            while (resultSet.next()) {
                sb.append("runtime:");
                sb.append(resultSet.getString("runtime")).append("\n");
                sb.append("country:");
                sb.append(resultSet.getString("country_name")).append("\n");
                sb.append("year_released:");
                sb.append(resultSet.getString("year_released")).append("\n");
                sb.append("title:");
                sb.append(resultSet.getString("title"));
                sb.append(System.lineSeparator());
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }


}
