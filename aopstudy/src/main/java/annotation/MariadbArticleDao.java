package annotation;

public class MariadbArticleDao implements ArticleDao{
	@Override
	public void insert(Article article) {
		System.out.println("MariadbArticleDao.insert() 메서드 호출. article:"+article);
	}

	@Override
	public void updateReadCount(int id, int i) {
		System.out.println("MariadbArticleDao.updateReadCount() 메서드 호출. int id:"+id + ", i:" + i);
	}
}

