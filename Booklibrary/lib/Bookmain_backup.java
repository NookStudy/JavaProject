import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.Scanner;


public class Bookmain_backup
{

	static Scanner sc = new Scanner(System.in);
	
	
	Connection con = null;
	String id;
	String pw;
	int memberno;
	
	public void connector() {
		try
		{
			Class.forName("oracle.jdbc.driver.OracleDriver");
			
			
		} catch (ClassNotFoundException cnfe)
		{
			cnfe.printStackTrace();
		}
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "booklib", "1234");
		}catch(Exception e){
			e.printStackTrace();
		}
			
		
	}
	public void disconnect()
	{
		try
		{
			if (con != null)
				con.close();
		} catch (SQLException sqle)
		{
			System.out.println();
		}
	}
	public static void main(String[] args)
	{
		Bookmain start = new Bookmain();
		
		start.firstMenu();
	}




	
	public void firstMenu()
	{
		System.out.println("도서관 정보통합시스템을 시작합니다.");
		while (true)
		{
			connector();
			int choice;
			
			System.out.println();
			System.out.println("[메뉴 선택]");
			System.out.println("1.로그인");
			System.out.println("2.회원가입");
			System.out.println();
			System.out.println("0.프로그램 종료");
			System.out.println();
			System.out.print("선택 : ");
			while (!sc.hasNextInt()) {
				sc.next();
				System.err.println("에러! 숫자가 아닙니다.");
				System.out.print("재 선택 : ");
			}	
			choice = sc.nextInt();
			sc.nextLine();
			switch (choice)
			{
			case 1:
				login();
				break;
			case 2:
				createMember();
				break;
		
			case 0:
				disconnect();
				System.out.println("프로그램을 종료합니다.");
				System.out.println("이용해 주셔서 감사합니다.");
				return;
	
			default:
				System.out.println("잘못 입력하셨습니다.");
				break;
			}
		}
	}
	
	public void login()
	{
		PreparedStatement idchk = null;
		ResultSet rsid = null;
		PreparedStatement login = null;
		ResultSet rs = null;
		String nickname;
		String pw;
		int exsistid;
		while(true) {
			try
			{
				//재입장시 초기화
				nickname = null;
				pw = null;
				memberno = 0;
				System.out.println();
				System.out.println("=== 로그인 ===");
				System.out.println("관리자메뉴는 admin을 통해 입장해주세요.");
				
				System.out.print("id :  ");
				nickname = sc.next();
				sc.nextLine();
				System.out.print("password :  ");
				pw = sc.next();
				sc.nextLine();
				String sql = "select * from members where nickname = ?";
				login = con.prepareStatement(sql);
				login.setString(1, nickname);
	//			login.setString(2, pw);
				rs = login.executeQuery();
//				String chkidsql = "select count(*) from members where nickname = ?";
//				idchk = con.prepareStatement(chkidsql);
//				idchk.setString(1, id);
//				rsid = idchk.executeQuery();
//				rsid.next();
//				exsistid = rsid.getInt(1);
				
//		System.out.println("확인1");
//				System.out.println("id존재? rs.next while :"+exsistid);
			
				//확인용
//				System.out.println(chkid);
//				System.out.println(chkpw);
//				if(exsistid==0) {
//				
//					break;
//				}else if(exsistid==1) {
				if(rs.next()) {
					String chkpw = rs.getString("pwd");
					String chkid = rs.getString("nickname");
//		System.out.println("확인2");
					//admin login
					if(nickname.equals("admin")) {	
						if(pw.equals(chkpw)){
							System.out.println( rs.getString("name")+"님의 입장을 환영합니다.");
							Statement memstmt = null;
							ResultSet rsmem = null;
							String memsql = "select member_id from members where lower(nickname)=lower('" + nickname + "')";
							memstmt = con.createStatement();
							rsmem = memstmt.executeQuery(memsql);
							rsmem.next();
//						System.out.println("회원번호"+rsmem.getString("member_id"));
							int memberId = rsmem.getInt("member_id");
							this.memberno = memberId;
							this.id = nickname;
							this.pw = pw;
							try{
								if(rs!=null)
									rs.close();
								if(rsid!=null)
									rsid.close();
								if (login != null)
									login.close();
								if (con != null)
									con.close();
								if (idchk != null)
									idchk.close();
								
								adminMenu();
								break;
							} catch (SQLException sqle){
								System.out.println();
							}
						break;
						}else {
							System.out.println("잘못된 비밀번호입니다.");
							break;
						}
						//일반회원 로그인
					}else if(!(nickname.equals("admin"))){
//		System.out.println("확인3");
//						System.out.println("id존재? :"+exsistid);
						if(pw.equals(chkpw)&& nickname.equals(chkid))	{
							System.out.println( rs.getString("name")+"님의 입장을 환영합니다.");
							//아이디를 통한 멤버NO 가져오기.
							Statement memstmt = null;
							ResultSet rsmem = null;
							String memsql = "select member_id from members where lower(nickname)=lower('" + nickname + "')";
							memstmt = con.createStatement();
							rsmem = memstmt.executeQuery(memsql);
							rsmem.next();
//							System.out.println("회원번호"+rsmem.getString("member_id"));
							int memberId = rsmem.getInt("member_id");
							
							try	{
								if(rs!=null)
									rs.close();
								if(rsid!=null)
									rsid.close();
								if (login != null)
									login.close();
								if (con != null)
									con.close();
								if (idchk != null)
									idchk.close();
								//멤버변수를 통해 로그인 이후에도 변수유지.
								this.id = nickname;
								this.pw = pw;
								this.memberno = memberId;
								membermenu();
								break;
							} catch (SQLException sqle){
								System.out.println();
							}
						}else if(nickname.equals(chkid)&&!(pw.equals(chkpw))) {							
							System.out.println("비밀번호가 잘못되었습니다.");
							break;
						}
					break;
					}
//					}
				}else
					System.out.println("id가 존재하지 않습니다. 회원가입을 진행해주세요. ");
			}catch (SQLException e){} 
		}
	}
//===============================================================admin===================	
	public void adminMenu()
	{
		System.out.println("관리자 메뉴 입장");
		while (true)
		{
			System.out.println();
			int choice;
			System.out.println("[관리자 메뉴]");
			System.out.println("1.도서 관리");
			System.out.println("2.도서 현황 조회");
			System.out.println("3.회원관리");
			System.out.println("4.비밀번호 재설정");
//			System.out.println("4.블랙리스트확인");
			System.out.println("0.관리자 메뉴 종료");
			System.out.println();
			System.out.print("선택 : ");
			while (!sc.hasNextInt()) {
				sc.next();
				System.err.println("에러! 숫자가 아닙니다.");
				System.out.print("재 선택 : ");
			}	
			choice = sc.nextInt();
			sc.nextLine();
			switch (choice)
			{
			case 1:
				bookControlMenu();
				break;
			case 2:
				bookSearchMenu();
				break;
			case 3:
				MemberControlMenu();
				break;
			case 4:
				Changepw();
				break;
				
//			case 4:
//				checkBlacklist();
//				break;
		
			case 0:
	
				System.out.println("관리자 메뉴를 종료합니다.");
				return;
	
			default:
				System.out.println("잘못 입력하셨습니다.");
				break;
			}
		}
	}
	public void bookControlMenu()
	{
		while(true) {
			System.out.println();
			System.out.println("[도서관리 메뉴 선택]");
			System.out.println("1.책 등록");
			System.out.println("2.책 삭제");
			System.out.println("0.메인메뉴로 돌아가기");
			System.out.print("선택 : ");
			while (!sc.hasNextInt()) {
				sc.next();
				System.err.println("에러! 숫자가 아닙니다.");
				System.out.print("재 선택 : ");
			}	
			int choice = sc.nextInt();
			sc.nextLine();
			switch (choice)
			{
			case 1:
				createBook();
				break;
			case 2:
				deleteBook();
				break;
			case 0:
				return;
	
			default:
				System.out.println("잘못 입력하셨습니다.");
				System.out.print("다시 입력하세요.");
				break;
			}
		}	
	}
	
	public static void createBook()
	{
		Connection con = null;
		Statement stmt = null;
		boolean success = false;
		try
		{

			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "booklib", "1234");
			stmt = con.createStatement();


			System.out.println("=== 도서 추가===");
			System.out.print("도서이름 : ");
			String name = sc.nextLine();

			String sql = "insert into Books values('','" + name + "','')";
			stmt.execute(sql);

			success = true;

		} catch (SQLException e)
		{
			System.out.println("오류");
			e.printStackTrace();
		} finally
		{
			try
			{
				if (success)
				{
					System.out.println("도서정보가 입력되었습니다.");
					con.commit();
				} else
				{
					System.out.println("도서정보 입력 실패");
					con.rollback();
				}
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (SQLException sqle)
			{
			}
		}
	}
	
	public void deleteBook()
	{
		Connection con = null;
		Statement stmt = null;
		boolean success = false;
		PreparedStatement chk_book = null;
		ResultSet rschkbook;
		try
		{

			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "booklib", "1234");
			stmt = con.createStatement();
			con.setAutoCommit(false);

			System.out.println("=== 도서 삭제===");
			System.out.print("도서id :  ");
			String id = sc.nextLine();
			
			String chkbooksql = "select count(*) from books where book_id = ?";
			chk_book = con.prepareStatement(chkbooksql);
			chk_book.setString(1, id);
			rschkbook = chk_book.executeQuery();
			rschkbook.next();
			int chkbook = rschkbook.getInt(1);
					
			
			if(chkbook==1) {
			String sql = "delete from Books where book_id='" + id + "'";
			stmt.execute(sql);

			success = true;
			
			}else
				System.out.println("삭제할 도서가 존재하지 않습니다.");

		} catch (SQLException e)
		{
			System.out.println("오류");
			e.printStackTrace();
		} finally
		{
			try
			{
				if (success)
				{
					con.commit();
					System.out.println("도서삭제가 완료되었습니다.");
				} else
				{
					System.err.println("도서정보 삭제 실패");
					con.rollback();
				}
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (SQLException sqle)
			{
				System.out.println();
			}
		}
	}
	public void bookSearchMenu()
	{
		while(true) {
			System.out.println();
			System.out.println("[도서찾기 메뉴 선택]");
			System.out.println("1.도서번호로 찾기");
			System.out.println("2.도서이름으로 찾기");
			System.out.println("3.모든도서 조회하기");
			System.out.println();
			System.out.println("0.이전메뉴로 돌아가기");
			System.out.println();
			System.out.print("선택 : ");
			
			while (!sc.hasNextInt()) {
				sc.next();
				System.err.println("에러! 숫자가 아닙니다.");
				System.out.print("재 선택 : ");
			}	
			int choice = sc.nextInt();
			sc.nextLine();
			switch (choice)
			{
			case 1:
				searchBookId();
				break;
			case 2:
				searchBookname();
				break;
			case 3:
				seachAllBook();
				break;
			case 0:
				return;
	
			default:
				System.out.println("잘못 입력하셨습니다.");
				System.out.print("다시 입력하세요.");
				break;
			}
		}	
	}
	public static void searchBookId()
	{
		PreparedStatement pstmt = null;
		boolean success = false;
		Connection con = null;
		ResultSet rs = null;
		try
		{

			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "booklib", "1234");

			con.setAutoCommit(false);

			System.out.println("=== 도서 조회===");
			System.out.print("도서번호 :  ");
			String id = sc.nextLine();

			String sql = "select book_id,book_name,bereturn_date from Books left outer join rental using (book_id)  where book_id='" + id + "'";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				System.out.print("도서번호 : " + rs.getString("book_id"));
				System.out.print(", 도서이름 : " + rs.getString("book_name"));
				String date = rs.getString("bereturn_date");
//				System.out.println(date);
				if(date==null)
					System.out.println(", 대여가능한 도서입니다.");
				else	
					System.out.println("," + rs.getString(3).substring(1,10)+"이후 대출이 가능합니다.");
			}else
				System.out.println("존재하지 않는 도서입니다.");

			System.out.println();

			success = true;

		} catch (SQLException e)
		{
			System.out.println("오류");
			e.printStackTrace();
		} finally
		{
			try
			{
				if (success)
				{
					System.out.println("도서정보 조회완료.");
					con.commit();
				} else
				{
					System.out.println("도서정보 조회 실패.");
					con.rollback();
				}
				if(rs!=null)
					rs.close();
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException sqle)
			{
				System.out.println();
			}
		}
	}
	public static void searchBookname()
	{
		PreparedStatement pstmt = null;
		boolean success = false;
		Connection con = null;
		ResultSet rs = null;
		
		//같은 도서이름의 총 권수 확인
		String chkbynamesql = "select count(*) from books where book_name = ?"
;		PreparedStatement chk_by_name = null;
		ResultSet rschkbyname = null;
		
		//대여 가능한 도서권수 호가인
		String chkposrent = "select count(*) from books where book_name =  ? and kwon = 1";
		PreparedStatement chk_pos_rent = null;
		ResultSet rschkposrent = null;
		
		try
		{

			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "booklib", "1234");

			con.setAutoCommit(false);

			System.out.println("=== 도서 조회===");
			System.out.print("도서이름 :  ");
			String name = sc.nextLine();
			
			
			chk_by_name = con.prepareStatement(chkbynamesql);
			chk_by_name.setString(1, name);
			rschkbyname = chk_by_name.executeQuery();
			rschkbyname.next();
			int totalkwon = rschkbyname.getInt(1);
			
			chk_pos_rent = con.prepareStatement(chkposrent);
			chk_pos_rent.setString(1, name);
			rschkposrent = chk_pos_rent.executeQuery();
			rschkposrent.next();
			int rentposkwon = rschkposrent.getInt(1);
			

			String sql = "select book_id,book_name,bereturn_date from Books left outer join rental using (book_id)  where book_name='" + name + "' order by book_id";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			
			if (rs.next())
			{
				do {
				System.out.print("도서번호 : " + rs.getString(1));
				System.out.print(", 도서이름 : " + rs.getString(2));
				String date = rs.getString("bereturn_date");
				if(date==null)
					System.out.println(", 대여가능 ");
				else	
					System.out.println(",반납예정일 : " + rs.getString(3).substring(0,10));
				}while(rs.next());

				System.out.println();
				System.out.printf("%s의 총 권수는 %d권 대여가능한 권수는 %d권 입니다.%n ",name,totalkwon,rentposkwon);
			}else
				System.out.println("도서가 없습니다.");

			success = true;

		} catch (SQLException e)
		{
			System.out.println("오류");
			e.printStackTrace();
		} finally
		{
			try
			{
				if (success)
				{
					System.out.println("도서정보 조회완료.");
					con.commit();
				} else
				{
					System.out.println("도서정보 조회 실패.");
					con.rollback();
				}
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
				if (rs != null)
					rs.close();
			} catch (SQLException sqle)
			{
				System.out.println();
			}
		}
	}
	public static void seachAllBook() {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		boolean success = false;
		String	sql = "select * from Books left outer join rental using (book_id) order by book_id";
		try
		{

			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "booklib", "1234");

			con.setAutoCommit(false);

			System.out.println("=== 모든 도서 조회===");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			while (rs.next())
			{
				System.out.printf("도서번호 : %3d",rs.getInt("book_id"));
				System.out.printf(", 도서이름 : %10s ",rs.getString("book_name"));
				String date = rs.getString("bereturn_date");
				if(date==null)
					System.out.println(", 대여가능 ");
				else	
					System.out.println(", 반납예정일 : " + rs.getString("bereturn_date").substring(0,10));
			}

			System.out.println();

			success = true;

		} catch (SQLException e)
		{
			System.out.println("오류");
			e.printStackTrace();
		} finally
		{
			try
			{
				if (success)
				{
					System.out.println("도서정보 조회완료.");
					con.commit();
				} else
				{
					System.out.println("도서정보 조회 실패.");
					con.rollback();
				}
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
				if (rs != null)
					rs.close();
			} catch (SQLException sqle)
			{
				System.out.println();
			}
		}
	}
	public void MemberControlMenu() 
	{
		while(true) {
			System.out.println();
			System.out.println("[회원관리 메뉴 선택]");
			System.out.println("1.회원 삭제");
			System.out.println("2.회원 조회");
			System.out.println();
			System.out.println("0.메인메뉴로 돌아가기");
			System.out.println();
			System.out.print("선택 : ");
			
			while (!sc.hasNextInt()) {
				sc.next();
				System.err.println("에러! 숫자가 아닙니다.");
				System.out.print("재 선택 : ");
			}	
			int choice = sc.nextInt();
			sc.nextLine();
			switch (choice)
			{
			case 1:
				deleteMember();
				break;
			case 2:
				searchMemberMenu();
				break;
			case 0:
				return;
	
			default:
				System.out.println("잘못 입력하셨습니다.");
				System.out.print("다시 입력하세요.");
				break;
			}
		}
	}
	public void deleteMember()
	{
		Connection con = null;
		Statement stmt = null;
		
		PreparedStatement chk_borrowed_books= null;
		ResultSet rschkBorrow = null;
		String chkBorrowedBooks = "select count(*) from rental where member_id = ?";
		int borrowedBooks;
		
		boolean success = false;
		try
		{

			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "booklib", "1234");
			stmt = con.createStatement();
			con.setAutoCommit(false);
			
			

			System.out.println("=== 회원 삭제===");
			System.out.print("회원번호 :  ");
			String memberid = sc.nextLine();
			
			chk_borrowed_books = con.prepareStatement(chkBorrowedBooks);
			chk_borrowed_books.setString(1, memberid);
			rschkBorrow = chk_borrowed_books.executeQuery();
			rschkBorrow.next();
			borrowedBooks = rschkBorrow.getInt("count(*)");
				if(borrowedBooks==0) {
					
				String sql = "delete members where member_id='" + memberid + "'";
				int updatecount = stmt.executeUpdate(sql);
					if(updatecount==1) {		
						System.out.println();
						success = true;
					}else
						System.out.println("존재하지 않는 회원입니다.");
	
				}else
					System.out.println("모든 도서를 반납해야 회원삭제가 가능합니다.");
		} catch (SQLException e)
		{
			System.out.println("오류");
			e.printStackTrace();
		} finally
		{
			try
			{
				if (success)
				{
					System.out.println("회원정보가 삭제되었습니다.");
					con.commit();
				} else
				{
					System.out.println("회원정보 삭제 실패");
					con.rollback();
				}
				if (stmt != null)
					stmt.close();
				if(chk_borrowed_books!=null)
					chk_borrowed_books.close();
				if(rschkBorrow!=null)
					rschkBorrow.close();
				if (con != null)
					con.close();
			} catch (SQLException sqle)
			{
				System.out.println();
			}
		}
	}
	public void searchMemberMenu()
	{
		while(true) {
			System.out.println();
			System.out.println("[회원찾기 메뉴 선택]");
			System.out.println("1.회원번호로 찾기");
			System.out.println("2.회원아이디로 찾기");
			System.out.println("3.회원이름으로 찾기");
			System.out.println("4.모든회원 조회하기");
			System.out.println("0.메뉴로 돌아가기");
			System.out.println();
			System.out.print("선택 : ");
			
			while (!sc.hasNextInt()) {
				sc.next();
				System.err.println("에러! 숫자가 아닙니다.");
				System.out.print("재 선택 : ");
			}	
			int choice = sc.nextInt();
			sc.nextLine();
			switch (choice)
			{
			case 1:
				searchMemberno();
				break;
			case 2:
				searchMemberID();
				break;
			case 3:
				searchMemberName();
				break;
			case 4:
				seachAllmembers();
				break;
			case 0:
				return;
	
			default:
				System.out.println("잘못 입력하셨습니다.");
				System.out.print("다시 입력하세요.");
				break;
			}	
		}
	}
//--------------------------------------------------------------------
	public static void searchMemberno()
	{
		PreparedStatement pstmt = null;
		boolean success = false;
		Connection con = null;
		ResultSet rs = null;
		try
		{

			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "booklib", "1234");

			con.setAutoCommit(false);

			System.out.println("=== 회원 조회===");
			System.out.print("회원번호 :  ");
			String id = sc.nextLine();

			String sql = "select * from members where member_id='" + id + "'";
			pstmt = con.prepareStatement(sql);
			rs = pstmt.executeQuery();
			if (rs.next())
			{
				System.out.print("회원번호 : " + rs.getString("Member_id"));
				System.out.print(", 회원아이디 : " + rs.getString("nickname"));
				System.out.print(", 회원이름 : " + rs.getString("name"));
				System.out.println(", 대여불가능날짜 : " + rs.getString("black"));
			}else
				System.out.println("존재하지 않는 회원입니다.");

			System.out.println();

			success = true;

		} catch (SQLException e)
		{
			System.out.println("오류");
			e.printStackTrace();
		} finally
		{
			try
			{
				if (success)
				{
					System.out.println("회원정보 조회완료.");
					con.commit();
				} else
				{
					System.out.println("회원정보 조회 실패.");
					con.rollback();
				}
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException sqle)
			{
				System.out.println();
			}
		}
	}
//-----------------------------------------------------------------
	public static void searchMemberID()
	{
		PreparedStatement pstmt = null;
		boolean success = false;
		Connection con = null;
		ResultSet rs = null;
		try
		{

			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "booklib", "1234");

			con.setAutoCommit(false);

			System.out.println("=== 회원 조회===");
			System.out.print("회원아이디 :  ");
			String nickname = sc.nextLine();

			String sql = "select * from members where nickname = ? ";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, nickname);
			rs = pstmt.executeQuery();
	
			if (rs.next()){
				do {
				System.out.print("회원번호 : " + rs.getString("member_id"));
				System.out.print(", 회원아이디 : " + rs.getString("nickname"));
				System.out.print(", 회원이름 : " + rs.getString("name"));
				System.out.println(", 대여 불가능날짜 : " + rs.getString("black"));
				}while(rs.next());
			}else
				System.out.println("존재하지 않는 회원입니다.");
			
			System.out.println();

			success = true;


		} catch (SQLException e)
		{
			System.out.println("오류");
			e.printStackTrace();
		} finally
		{
			try
			{
				if (success)
				{
					System.out.println("회원정보 조회완료.");
					con.commit();
				} else
				{
					System.out.println("회원정보 조회 실패.");
					con.rollback();
				}
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException sqle)
			{
				System.out.println();
			}
		}
	}
//-----------------------------------------------------------------------
	public static void seachAllmembers() {
		Connection con = null;
		Statement stmt = null;
		ResultSet rs = null;
		boolean success = false;
		String	sql = "select * from members";
		try
		{

			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "booklib", "1234");

			con.setAutoCommit(false);

			System.out.println("=== 모든 회원 조회===");
			stmt = con.createStatement();
			rs = stmt.executeQuery(sql);
			if (rs.next())
			{
				do {
				System.out.print("회원번호 : " + rs.getString("member_id"));
				System.out.print(", 회원아이디 : " + rs.getString("nickname"));
				System.out.println(", 회원이름 : " + rs.getString("name"));
//				System.out.println(", 블랙리스트여부 : " + rs.getString("black"));
				}while(rs.next());
			}else{
				System.out.println("회원이 존재하지 않습니다.");
			}
			System.out.println();

			success = true;

		} catch (SQLException e)
		{
			System.out.println("오류");
			e.printStackTrace();
		} finally
		{
			try
			{
				if (success)
				{
					System.out.println("회원정보 조회완료.");
					con.commit();
				} else
				{
					System.out.println("회원정보 조회 실패.");
					con.rollback();
				}
				if (stmt != null)
					stmt.close();
				if (con != null)
					con.close();
			} catch (SQLException sqle)
			{
				System.out.println();
			}
		}
	}
	
	public void searchMemberName()
	{
		PreparedStatement pstmt = null;
		boolean success = false;
		Connection con = null;
		ResultSet rs = null;
		try
		{

			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "booklib", "1234");

			con.setAutoCommit(false);

			System.out.println("=== 회원 조회===");
			System.out.print("회원이름 :  ");
			String name = sc.nextLine();

			String sql = "select * from members where name = ? ";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, name);
			rs = pstmt.executeQuery();
	
			if (rs.next()){
				do {
				System.out.print("회원번호 : " + rs.getString("member_id"));
				System.out.print(", 회원아이디 : " + rs.getString("nickname"));
				System.out.print(", 회원이름 : " + rs.getString("name"));
				System.out.println(", 대여 불가능날짜 : " + rs.getString("black"));
				}while(rs.next());
			}else
				System.out.println("존재하지 않는 회원입니다.");
			
			System.out.println();

			success = true;


		} catch (SQLException e)
		{
			System.out.println("오류");
			e.printStackTrace();
		} finally
		{
			try
			{
				if (success)
				{
					System.out.println("회원정보 조회완료.");
					con.commit();
				} else
				{
					System.out.println("회원정보 조회 실패.");
					con.rollback();
				}
				if (pstmt != null)
					pstmt.close();
				if (con != null)
					con.close();
			} catch (SQLException sqle)
			{
				System.out.println();
			}
		}
		
	}
//============================================================
//	public static void checkBlacklist() {
//		Connection con = null;
//		PreparedStatement pstmt = null;
//		ResultSet rs = null;
//		boolean success = false;
//		String	sql = "select * from members where blackcheck = 0";
//		try
//		{
//
//			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "booklib", "1234");
//
//			con.setAutoCommit(false);
//
//			System.out.println("=== 블랙리스트 조회===");
//			pstmt = con.prepareStatement(sql);
//				
//			rs = pstmt.executeQuery();
//			while (rs.next())
//			{
//				System.out.print("회원번호 : " + rs.getString("member_id"));
//				System.out.print(", 회원아이디 : " + rs.getString("nickname"));
//				System.out.print(", 회원이름 : " + rs.getString("name"));
//				System.out.println(", 블랙리스트여부 : " + rs.getString("black"));
//			}
//			System.out.println();
//
//			success = true;
//
//		} catch (SQLException e)
//		{
//			System.out.println("오류");
//			e.printStackTrace();
//		} finally
//		{
//			try
//			{
//				if (success)
//				{
//					System.out.println("회원정보 조회완료.");
//					con.commit();
//				} else
//				{
//					System.out.println("회원정보 조회 실패.");
//					con.rollback();
//				}
//				if (pstmt != null)
//					pstmt.close();
//				if (con != null)
//					con.close();
//			} catch (SQLException sqle)
//			{
//				System.out.println();
//			}
//		}
//	}
//====================================admin end===============================================
//create member
	public void createMember()
	{
		Connection con = null;
		PreparedStatement add_memeber =null;
		PreparedStatement exsist_id = null;
		ResultSet rsExsist = null;
		boolean success = false;
		try
		{
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "booklib", "1234");
			String sql = "insert into members values('',?,?,?,'')";
			add_memeber = con.prepareStatement(sql);
			String existid = "select count(*) from members where nickname = ? ";
			exsist_id = con.prepareStatement(existid);
			int updatecount =0;
			System.out.println("=== 회원 가입===");
			while(updatecount==0)
			{
				System.out.println("(아이디는 영문,숫자로 5~18자리로 입력해주세요.)");
				System.out.print("사용하실 아이디를 입력해주세요 :");
				String nickname;
				nickname = sc.nextLine();
				exsist_id.setString(1, nickname);
				rsExsist = exsist_id.executeQuery();
				
				while(rsExsist.next()) {
					int countid = rsExsist.getInt(1);
					//확인용
//					System.out.println(countid);
					if(countid==0 && nickname.length()>=5 && nickname.length()<=18 ) {
						System.out.println("사용가능한 ID입니다.");
						System.out.print("회원님의 이름을 입력해 주세요 : ");
						String name = sc.nextLine();
						String pw;
						String chkpw;
						do{ 
							System.out.print("사용하실 비밀번호를 입력해주세요 : ");
							pw = sc.nextLine();
							System.out.print("비밀번호를 한번 더 입력해 주세요. : ");
							chkpw = sc.nextLine();
							if(pw.equals(chkpw)){
								con.prepareStatement(sql);
								add_memeber.setString(1, nickname);
								add_memeber.setString(2, pw);
								add_memeber.setString(3, name);
								updatecount = add_memeber.executeUpdate();
								
								success = true;
								disconnect();
								try
								{
									if (success)
									{
										System.out.println("회원가입이 완료되었습니다.");
										con.commit();
										if (exsist_id != null)
											exsist_id.close();
										if (add_memeber != null)
											add_memeber.close();
										if (rsExsist != null)
											rsExsist.close();
										if (con != null)
											con.close();
										break;
									} else
									{
										System.out.println("회원가입 실패");
										con.rollback();
									}
								} catch (SQLException sqle){}
							}else {
								System.err.println("입력하신 비밀번호가 다릅니다!!");
							}
						}while(!(pw.equals(chkpw)));
						break;
					}else if(countid==1) {
						System.out.println("이미 존재하는 아이디입니다.");
						System.out.println("다른 아이디를 입력해 주세요");
						break;
					}else if(countid==0&& (nickname.length()<5 || nickname.length()>18)){
						System.out.println("아이디의 길이가 적절하지 않습니다.");
						System.out.print("다른 아이디를 입력해주세요 :");
					}
				}	
			}
		} catch (SQLException e){
			System.err.println("회원가입에 실패하셨습니다. 관리자에게 문의하세요");
			e.printStackTrace();
		} 
		
	}
	
	
	
//====================================member start============================================	
	public void membermenu() {
		while(true){
		System.out.println();
		System.out.println("[회원 메뉴 선택]");
		System.out.println("1.책 대여");
		System.out.println("2.책 반납");
		System.out.println("3.책 정보 찾기");
		System.out.println("4.대여기간 연장");
		System.out.println("5.비밀번호 재설정");
		System.out.println();
		
		System.out.println("0.메인메뉴로 돌아가기");
		System.out.println();
		System.out.print("선택 :");
		while (!sc.hasNextInt()) {
			sc.next();
			System.err.println("에러! 숫자가 아닙니다.");
			System.out.print("재 선택 : ");
		}	
		int choice = sc.nextInt();
		sc.nextLine();
			switch (choice)
			{
			case 1:
				rentBook();
				break;
			case 2:
				returnBook();
				break;
			case 3:
				bookSearchMenu();
				break;
			case 4:
				delayRent();
				break;
			case 5:
				Changepw();
				break;
			case 0:
				return;
	
			default:
				System.out.println("잘못 입력하셨습니다.");
				break;
			}
		}	
	}
	
	
	
	
	
	public void rentBook()
	{
		Connection con = null;
		
		
		//권수를 확인할 Statㄷment 선언
		
		PreparedStatement chkkwonstmt =null;
		String chkKown = "select kwon from books where book_id = ?";
		ResultSet rscheck = null;
		
		
		PreparedStatement chk_borrowed_books= null;
		ResultSet rschkBorrow = null;
		String chkBorrowedBooks = "select count(*) from rental where member_id = ?";
		int borrowedBooks;
		
		String bookid;
		PreparedStatement chkblack = null;
		ResultSet rschkblack = null;
		String black = "select black from members where member_id = ?";
		
		
		
		PreparedStatement finalstmt = null;
		PreparedStatement controlkwon = null;
		boolean success = false;
		try
		{

			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "booklib", "1234");
			con.setAutoCommit(false);
			
			
			//책이존재하면
				//black 회원인지 조회 (날짜비교)
				chkblack = con.prepareStatement(black);
				chkblack.setInt(1, memberno);
				rschkblack = chkblack.executeQuery();
				rschkblack.next();
				//오라클 날짜 받아서 자바날짜로 변환
				String blackDate = rschkblack.getString(1);
				String modifiedBlackDate = blackDate.substring(0, 10);
				LocalDate oracleblackDate = LocalDate.parse(modifiedBlackDate, DateTimeFormatter.ISO_DATE);
				LocalDate today = LocalDate.now(); 
				if (today.isBefore(oracleblackDate)||today.isEqual(oracleblackDate)) {
					System.out.println("대여일수를 초과하여 반납하신 이력이 있습니다.");
					System.out.println(oracleblackDate+"이후부터 대여가 가능합니다.");
					
				}else if(today.isAfter(oracleblackDate)) {
					System.out.println("=== 도서 대여 ===");
					System.out.print("도서번호 : ");
					bookid = sc.nextLine();
					
					//책이 존재하면
					
					chkkwonstmt = con.prepareStatement(chkKown);
					chkkwonstmt.setString(1, bookid);
					rscheck =  chkkwonstmt.executeQuery();
					if(rscheck.next()) {
						//총 몇권 빌렸는지 확인
						chk_borrowed_books = con.prepareStatement(chkBorrowedBooks);
						chk_borrowed_books.setInt(1, memberno);
						rschkBorrow = chk_borrowed_books.executeQuery();
						rschkBorrow.next();
						borrowedBooks = rschkBorrow.getInt(1);
						
						//권수 체크로 대여가능한지 확인
						String kwon =rscheck.getString(1);
						
			//			System.out.println("권수"+kwon);
						if((Integer)Integer.parseInt(kwon)==1 && borrowedBooks<=5) 
						{
							
							//렌탈DB에 등재
							String finalsql = "insert into rental values('',?,?,sysdate,sysdate+7)";
							finalstmt = con.prepareStatement(finalsql);
							finalstmt.setInt(1, memberno);
							finalstmt.setString(2, bookid);
							int updateno = finalstmt.executeUpdate();
							
							//BOOKDB 권수 down
							if(updateno==1) {
							String contkwon = "update books set kwon = '0' where book_id = ? ";
							controlkwon = con.prepareStatement(contkwon);
							controlkwon.setString(1, bookid);
							controlkwon.executeUpdate();
							success = true;
							}
						}else if((Integer)Integer.parseInt(kwon)==0) {
							System.err.println("이미 대여된 도서입니다.");
						}else if(borrowedBooks>5) 
							System.err.println("최대도서대여수를 초과하셨습니다. 대여가 불가능합니다.");
					}else
						System.err.println("등록되지 않은 도서입니다.");
			}	
		} catch (SQLException e){
			System.out.println("오류");
			e.printStackTrace();
		} finally{
			try
			{
				if (success){
					System.out.println("도서대여가 완료되었습니다.");
					con.commit();
				} else{
					System.err.println("도서대여 실패");
					con.rollback();
				}
				if (con != null)
					con.close();
				if (finalstmt != null)
					finalstmt.close();
				if (chkkwonstmt != null)
					chkkwonstmt.close();
				if (chk_borrowed_books != null)
					chk_borrowed_books.close();
				if (chkblack != null)
					chkblack.close();
				if (rscheck != null)
					rscheck.close();
				if (rschkblack != null)
					rschkblack.close();
				if (rschkBorrow != null)
					rschkBorrow.close();
				return;
			} catch (SQLException sqle)
			{}
		}
	}
	
	public void returnBook()
	{
		
		Connection con = null;
		PreparedStatement controlkwon = null;
		PreparedStatement finalstmt = null;
		
		PreparedStatement chk_BeReturndate = null;
		ResultSet chkbereturndate = null;
		
		PreparedStatement chkkwonstmt =null;
		String chkKown = "select kwon from books where book_id = ?";
		ResultSet rscheck = null;
		
		boolean success = false;
		try
		{

			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "booklib", "1234");
			con.setAutoCommit(false);

			System.out.println("=== 도서 반납 ===");
			System.out.print("도서번호 : ");
			int book_id = sc.nextInt();
			sc.nextLine();
			
			//권수로 빌려진 도서인지 확인
			chkkwonstmt = con.prepareStatement(chkKown);
			chkkwonstmt.setInt(1, book_id);
			rscheck =  chkkwonstmt.executeQuery();
			rscheck.next();
			String kwon =rscheck.getString(1);
			
			if((Integer)Integer.parseInt(kwon)==0){
				//반납일자와 반납예정일자 비교
				String compare = "select BERETURN_DATE from rental where book_id = ?";
				chk_BeReturndate = con.prepareStatement(compare);
				chk_BeReturndate.setInt(1, book_id);
				chkbereturndate = chk_BeReturndate.executeQuery();
				chkbereturndate.next();
				String bereturndate = chkbereturndate.getString(1);
//				System.out.println(bereturndate);
				//오라클 날짜를 잘라서 자바로 Date로 변환
			    String modifiedreturndate = bereturndate.substring(0, 10);
//			    System.out.println(modifiedreturndate);
			    LocalDate today = LocalDate.now(); 
			    LocalDate javaBereturnDate = LocalDate.parse(modifiedreturndate, DateTimeFormatter.ISO_DATE);
			    long daysDiff = ChronoUnit.DAYS.between(javaBereturnDate,today); // 날짜 차이 계산
//			    System.out.println(daysDiff);
			    
			    if (today.isAfter(javaBereturnDate)) {
			        System.out.println("반납기간을"+daysDiff+"일 초과하셨습니다!!"); // 오늘 날짜가 오라클에서 받아온 날짜보다 이후인 경우
			        
			        String finalsql = "delete from rental where book_id = ?";
			        finalstmt = con.prepareStatement(finalsql);
			        finalstmt.setInt(1, book_id);
			        finalstmt.executeUpdate();
			        
			        
			        String contkwon = "update books set kwon = '1' where book_id = ? ";
			        controlkwon = con.prepareStatement(contkwon);
			        controlkwon.setInt(1, book_id);
			        controlkwon.executeUpdate();
			      
			        
			        Statement black = null;
			        String blacksql = "update members set black = sysdate+"+daysDiff*2+" where member_id = "+memberno;
			        black = con.createStatement();
			        black.executeUpdate(blacksql);
			        System.out.println(today.plusDays(daysDiff*2)+"일 이후부터 도서대여가 가능합니다.");
			    } else if (today.isEqual(javaBereturnDate)||today.isBefore(javaBereturnDate)) {
			        System.out.println("반납기간 이내 반납입니다."); // 오늘 날짜가 오라클에서 받아온 날짜와 동일한 경우
			        
			        String finalsql = "delete from rental where book_id = ?";
			        finalstmt = con.prepareStatement(finalsql);
			        finalstmt.setInt(1, book_id);
			        finalstmt.executeUpdate();
			        
			        
			        String contkwon = "update books set kwon = '1' where book_id = ? ";
			        controlkwon = con.prepareStatement(contkwon);
			        controlkwon.setInt(1, book_id);
			        controlkwon.executeUpdate();
			        
			    }
			}
			success = true;

		} catch (SQLException e)
		{
			System.out.println("오류");
			e.printStackTrace();
		} finally
		{
			try
			{
				if (success)
				{
					System.out.println("도서반납이 완료되었습니다.");
					con.commit();
				} else
				{
					System.out.println("도서반납 실패");
					con.rollback();
				}
				if (finalstmt != null)
					finalstmt.close();
				if (con != null)
					con.close();
				return;
			} catch (SQLException sqle)
			{
			}
		}
	}	
	
	public void delayRent()
	
	{
		Connection con = null;
		//대여일 확인
		PreparedStatement chkRentDate =null;
		String chkRentsql = "select rental_date from rental where book_id = ?";
		ResultSet rschkRent = null;
		
		

		//반납예정일확인
		PreparedStatement chkbackdate = null;
		String chkbacksql = "select bereturn_date from rental where book_id = ?";
		ResultSet rschkback = null;
		
		
		//대여일자 연장
		PreparedStatement finalstmt = null;
		String finalsql = "update rental set bereturn_date = ? where book_id = ?";
		
		//대여중인 책인지 확인
		PreparedStatement chk_borrowed_books= null;
		ResultSet rschkBorrow = null;
		int borrowedBooks;
		String chkBorrowedBooks = "select count(*) from rental where book_id = ?";
		
		String bookid;
		boolean success = false;
//		SimpleDateFormat simpleDateFormat = new	 SimpleDateFormat("yy/MM/dd"); 
		
		try
		{
			
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "booklib", "1234");
			con.setAutoCommit(false);
			
			
			
			System.out.println("=== 대여 연장 ===");
			System.out.print("연장하시려는 도서번호를 입력하세요 : ");
			bookid = sc.nextLine();
			
			chk_borrowed_books = con.prepareStatement(chkBorrowedBooks);
			chk_borrowed_books.setString(1, bookid);
			rschkBorrow = chk_borrowed_books.executeQuery();
			rschkBorrow.next();
			borrowedBooks = rschkBorrow.getInt(1);
			if(borrowedBooks==0)
				System.out.println("대여중인 도서가 아닙니다.");
			else if(borrowedBooks==1){
				//반납예정일 가져오기
				chkbackdate = con.prepareStatement(chkbacksql);
				chkbackdate.setString(1, bookid);
				rschkback = chkbackdate.executeQuery();
				rschkback.next();
				String backDate = rschkback.getString(1);
	//			System.out.println(backDate);
				String modifiedBackDate = backDate.substring(0, 10);
				LocalDate javabackDate = LocalDate.parse(modifiedBackDate, DateTimeFormatter.ISO_DATE);
				LocalDate delaydate = javabackDate.plusWeeks(1);
				String strdelayedDate = delaydate.toString();
				String modistrdelayedDate = strdelayedDate.substring(0, 10);
	//			System.out.println(strdelayedDate);
				//대여날짜 확인
				chkRentDate = con.prepareStatement(chkRentsql);
				chkRentDate.setString(1, bookid);
				rschkRent =  chkRentDate.executeQuery();
				rschkRent.next();
				String rentDate = rschkRent.getString(1);
				String modifiedRentDate = rentDate.substring(0, 10);
				LocalDate oracleRentDate = LocalDate.parse(modifiedRentDate, DateTimeFormatter.ISO_DATE);
				long daysDiff = ChronoUnit.DAYS.between(oracleRentDate, javabackDate); // 날짜 차이 계산
	//			System.out.println(daysDiff);
				LocalDate today = LocalDate.now();
				
				
					
				//반납예정일과 대여날짜를 비교하여 8일 이상이면 1회 연장한것이므로 연장불가.
				if (daysDiff<8 || today.isBefore(javabackDate) ) {
					finalstmt = con.prepareStatement(finalsql);
					finalstmt.setString(1, modistrdelayedDate);
					finalstmt.setString(2, bookid);
					int updatecount=finalstmt.executeUpdate();
					
					if(updatecount==1) { 
						success = true;
					}	
					else
						System.err.println("도서연장 실패"); 
				}else if(today.isAfter(javabackDate)||daysDiff>8) {
					System.out.println("연장을 이미 하셨거나 반납일을 이미 지나 연장이 불가능합니다. 죄송합니다. ");
				}else
					System.out.println("잘못입력하셨습니다.");
			}
		} catch (SQLException e){
			System.out.println("오류");
			e.printStackTrace();
		} finally{
			try
			{
				if (success){
					System.out.println("대여 연장이 완료되었습니다.");
					con.commit();
				} else{
					System.out.println("대여 연장 실패");
					con.rollback();
				}
				if (finalstmt != null)
					finalstmt.close();
				if (con != null)
					con.close();
				if (chkRentDate != null)
					chkRentDate.close();
				if (chkbackdate != null)
					chkbackdate.close();
				if (rschkback != null)
					rschkback.close();
				if (rschkRent != null)
					rschkRent.close();
				if(rschkBorrow!=null)
					rschkBorrow.close();
				if(chk_borrowed_books!=null)
					chk_borrowed_books.close();
				return;
			} catch (SQLException sqle)
			{}
		}
	}
		
	public void Changepw()
	{
		Connection con = null;
		PreparedStatement updatestmt = null;
		String updatesql = "update members set pwd = ? where member_id = ?";
		
		PreparedStatement login = null;
		String sql = "select pwd from members where member_id = ?";
		ResultSet rs = null;
		System.out.println("===비밀번호 변경===");
		System.out.println("본인확인을 위해 비밀번호를 재입력해 주세요");
		String paswd = sc.nextLine();
//		System.out.println("확인1");
		
		String chkpw;
		String resetpw;
		boolean success = false;
		try {
			con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1521:xe", "booklib", "1234");
			con.setAutoCommit(false);
			login = con.prepareStatement(sql);
			login.setInt(1, memberno);
//		System.out.println("확인2");
			rs = login.executeQuery();
//		System.out.println("확인3");
			rs.next();
//		System.out.println("확인4");
			String chkfirstpw = rs.getString("pwd");
//		System.out.println(chkfirstpw);
			if(paswd.equals(chkfirstpw)) {
				do{ 
					System.out.print("재설정할 비밀번호를 입력해주세요. : ");
					resetpw = sc.nextLine();
					System.out.print("비밀번호를 한번 더 입력해 주세요. : ");
					chkpw = sc.nextLine();
					if(resetpw.equals(chkpw)){
						updatestmt =con.prepareStatement(updatesql);
						updatestmt.setString(1, resetpw);
						updatestmt.setInt(2, memberno);
						int count = updatestmt.executeUpdate();
						if(count==1)
							success = true;
						break;
					}else {
						System.err.println("입력하신 비밀번호가 다릅니다!!");
					}
				}while(!(paswd.equals(chkpw)));
				
			}else
				System.out.println("비밀번호를 틀렸습니다.");
			
		}catch (Exception e) {
		}finally{
			try
			{
				if (success){
					System.out.println("비밀번호 재설정이 완료되었습니다.");
					con.commit();
				} else{
					System.out.println("비밀번호 재설정 실패");
					con.rollback();
				}
				if (con != null)
					con.close();
				if (login != null)
					login.close();
				if (updatestmt != null)
					updatestmt.close();
				return;
			} catch (SQLException sqle)
			{}
		
		}
	}	
}	
