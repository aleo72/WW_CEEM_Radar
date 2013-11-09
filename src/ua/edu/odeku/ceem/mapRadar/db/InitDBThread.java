package ua.edu.odeku.ceem.mapRadar.db;

/**
 * Данный класс Производит обращение к базе данных при старте программы в отдельном потоке.
 * Данное обращение инициализирует базу данных
 *
 * User: Aleo skype: aleo72
 * Date: 09.11.13
 * Time: 21:46
 */
public class InitDBThread extends Thread {

    @Override
    public void run() {
        DB.getEntityManager().close();
    }
}
