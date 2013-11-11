package ua.edu.odeku.ceem.mapRadar.tools.viewGeoName;

import ua.edu.odeku.ceem.mapRadar.db.DB;
import ua.edu.odeku.ceem.mapRadar.db.models.GeoName;
import ua.edu.odeku.ceem.mapRadar.resource.ResourceString;

import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import javax.swing.event.TableModelListener;
import javax.swing.table.AbstractTableModel;
import java.util.List;
import java.util.TreeSet;

/**
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

    @Override
    public int getRowCount() {
        return list.size();
    }

    @Override
    public int getColumnCount() {
        return 9;
    }

    @Override
    public Object getValueAt(int rowIndex, int columnIndex) {
        GeoName geoName = list.get(rowIndex);
        switch (columnIndex){
            case 0 :
                return rowIndex;
            case 1 :
                return geoName.getName();
            case 2 :
                return geoName.getAsciiname();
            case 3 :
                return geoName.getAlternatenames();
            case 4 :
                return geoName.getCountryCode();
            case 5 :
                return geoName.getFeatureClass();
            case 6 :
                return geoName.getFeatureCode();
            case 7 :
                return geoName.getLat();
            case 8 :
                return geoName.getLon();
            default:
                return null;
        }
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
                return String.class;
            case 7 :
                return Double.class;
            case 8 :
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
                return ResourceString.get("table_alternate_names");
            case 4:
                return ResourceString.get("table_country_code");
            case 5:
                return ResourceString.get("table_feature_class");
            case 6:
                return ResourceString.get("table_feature_code");
            case 7:
                return ResourceString.get("table_lat");
            case 8:
                return ResourceString.get("table_lon");
            default:
                return null;

        }
    }
}
