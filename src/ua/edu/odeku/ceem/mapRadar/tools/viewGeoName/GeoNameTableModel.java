package ua.edu.odeku.ceem.mapRadar.tools.viewGeoName;

import org.hibernate.SQLQuery;
import org.hibernate.ScrollMode;
import org.hibernate.ScrollableResults;
import org.hibernate.Session;
import ua.edu.odeku.ceem.mapRadar.db.DB;
import ua.edu.odeku.ceem.mapRadar.db.models.GeoName;
import ua.edu.odeku.ceem.mapRadar.resource.ResourceString;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

/**
 * Модель для таблицы, данная модель заполняет таблицу изходя из переданых ей параметров
 * User: Aleo skype: aleo72
 * Date: 10.11.13
 * Time: 21:26
 */
public class GeoNameTableModel extends AbstractTableModel {

    private List<GeoName> list;

    public GeoNameTableModel(){

        EntityManager entityManager = DB.getEntityManager();
        entityManager.getTransaction().begin();

        CriteriaBuilder cb = entityManager.getCriteriaBuilder();
        CriteriaQuery<GeoName> criteriaQuery = cb.createQuery(GeoName.class);
        Root<GeoName> from = criteriaQuery.from(GeoName.class);

        list = entityManager.createQuery(criteriaQuery.select(from)).getResultList();

        entityManager.getTransaction().commit();
    }

    public GeoNameTableModel(String subName, String country, String featureClass, String featureCode){
        Session session = DB.getSession();

        String select = "SELECT {G.*} FROM GEO_NAME G WHERE 1 = 1 ";

        if(country != null && !country.isEmpty()){
            select += " AND G.COUNTRY_CODE = :country ";
        }
        if(featureClass != null && !featureClass.isEmpty()){
            select += " AND G.FEATURE_CLASS = :featureClass ";
            if(featureCode != null && !featureCode.isEmpty()){
                select += " AND G.FEATURE_CODE = :featureCode ";
            }
        }
        select += ";";

        SQLQuery query = session.createSQLQuery(select);
        query.addEntity("G", GeoName.class);

        if(country != null && !country.isEmpty()){
            query.setParameter("country", country);
        }
        if(featureClass != null && !featureClass.isEmpty()){
            select += " AND G.FEATURE_CLASS = :featureClass ";
            query.setParameter("featureClass", featureClass);
            if(featureCode != null && !featureCode.isEmpty()){
                select += " AND G.FEATURE_CODE = :featureCode ";
                query.setParameter("featureCode", featureCode);
            }
        }

        if(subName == null || subName.isEmpty()){
            list = handlerScrollableResults(query);
        } else {
            list = handlerScrollableResults(query, subName);
        }

        DB.closeSession(session);
    }

    private List<GeoName> handlerScrollableResults(SQLQuery query, String prefixName) {

        List<GeoName> listName = new LinkedList<GeoName>();
        List<GeoName> listASCIIName = new LinkedList<GeoName>();
        List<GeoName> listTranslate = new LinkedList<GeoName>();
        List<GeoName> listAlternative = new LinkedList<GeoName>();
        String prefix = prefixName.toLowerCase();

        ScrollableResults results = query.scroll(ScrollMode.FORWARD_ONLY);
        results.beforeFirst();

        while(results.next()){
            GeoName geoName = (GeoName) results.get(0);
            // Проверяем название
            if(geoName.getName().toLowerCase().startsWith(prefix)){
                listName.add(geoName);

                // Проверим АСКИ название
            } else if (geoName.getAsciiname().toLowerCase().startsWith(prefix)){
                listASCIIName.add(geoName);

                // Проверим перевод
            } else if (geoName.getTranslateName() != null && !geoName.getTranslateName().isEmpty() &&
                    geoName.getTranslateName().toLowerCase().startsWith(prefix)){
                listTranslate.add(geoName);

                // проверим альтернативные названия
            } else if (geoName.getAlternatenames() != null && !geoName.getAlternatenames().isEmpty()) {
                for(String alt : geoName.getAlternatenamesArray()){
                    if (alt.toLowerCase().startsWith(prefix)){
                        listAlternative.add(geoName);
                        break;
                    }
                }
            }
        }


        ArrayList<GeoName> list =
                new ArrayList<>(listName.size() + listASCIIName.size() + listTranslate.size() + listTranslate.size());

        list.addAll(listName);
        list.addAll(listASCIIName);
        list.addAll(listTranslate);
        list.addAll(listAlternative);

        return list;
    }

    private List<GeoName> handlerScrollableResults(SQLQuery sqlQuery){
        List<GeoName> list = new ArrayList<GeoName>(50000);
        ScrollableResults results = sqlQuery.scroll(ScrollMode.FORWARD_ONLY);
        results.beforeFirst();
        while(results.next()){
            list.add((GeoName) results.get(0));
        }
        return list;
    }

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 10;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        if (list.size() > rowIndex){
            GeoName geoName = list.get(rowIndex);
            switch (columnIndex){
                case 0 :
                    return rowIndex;
                case 1 :
                    return geoName.getName();
                case 2 :
                    return geoName.getAsciiname();
                case 3 :
                    return geoName.getTranslateName();
                case 4 :
                    return geoName.getAlternatenames();
                case 5 :
                    return geoName.getCountryCode();
                case 6 :
                    return geoName.getFeatureClass();
                case 7 :
                    return geoName.getFeatureCode();
                case 8 :
                    return geoName.getLat();
                case 9 :
                    return geoName.getLon();
                default:
                    return null;
            }
        }
        return null;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch (columnIndex){
            case 0 :
                return Integer.class;
            case 1 :
                return String.class;
            case 2 :
                return String.class;
            case 3 :
                return String.class;
            case 4 :
                return String.class;
            case 5 :
                return String.class;
            case 6 :
                return Character.class;
            case 7 :
                return String.class;
            case 8 :
                return Double.class;
            case 9 :
                return Double.class;
            default:
                return null;
        }
    }

    @Override
    public String getColumnName(int column) {
        switch (column){
            case 0:
                return ResourceString.get("table_row");
            case 1:
                return ResourceString.get("table_name");
            case 2:
                return ResourceString.get("table_asciiname");
            case 3:
                return ResourceString.get("table_translate");
            case 4:
                return ResourceString.get("table_alternate_names");
            case 5:
                return ResourceString.get("table_country_code");
            case 6:
                return ResourceString.get("table_feature_class");
            case 7:
                return ResourceString.get("table_feature_code");
            case 8:
                return ResourceString.get("table_lat");
            case 9:
                return ResourceString.get("table_lon");
            default:
                return null;

        }
    }

    public List<GeoName> getListGeoName(){
        return list;
    }
}
