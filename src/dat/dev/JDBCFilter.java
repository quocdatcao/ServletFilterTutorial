package dat.dev;
 
import java.io.IOException;
import java.sql.Connection;
 
import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
 
@WebFilter(urlPatterns = { "/*" })
public class JDBCFilter implements Filter {
 
    public JDBCFilter() {
    }
 
    @Override
    public void init(FilterConfig fConfig) throws ServletException {
 
    }
 
    @Override
    public void destroy() {
 
    }
 
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
 
        HttpServletRequest req = (HttpServletRequest) request;
 
        // 
        String servletPath = req.getServletPath();
 
      
        if (servletPath.contains("/specialPath1") || servletPath.contains("/specialPath2")) {
            Connection conn = null;
            try {
               
                conn = ConnectionUtils.getConnection();
                
                conn.setAutoCommit(false);
 
                MyUtils.storeConnection(request, conn);
 
              
                chain.doFilter(request, response);
 
                
                conn.commit();
            } catch (Exception e) {
                ConnectionUtils.rollbackQuietly(conn);
                throw new ServletException();
            } finally {
                ConnectionUtils.closeQuietly(conn);
            }
        }
     
        else {
          
            chain.doFilter(request, response);
        }
 
    }
 
}