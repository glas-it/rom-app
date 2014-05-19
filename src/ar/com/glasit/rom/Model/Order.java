package ar.com.glasit.rom.Model;

import ar.com.glasit.rom.Helpers.BackendHelper;
import ar.com.glasit.rom.Service.RestService;
import ar.com.glasit.rom.Service.WellKnownKeys;
import ar.com.glasit.rom.Service.WellKnownMethods;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

public class Order {

    private static final String ID = "id";
    private static final String RUBRO = "idRubro";
    private static final String SUBRUBRO = "idSubrubro";
    private static final String PRODUCT = "idConsumicion";
    private static final String ADDITION = "idAgregado";
    private static final String PRICE = "precio";
    private static final String STATUS = "estado";
    private static final String NOTES = "observaciones";

    public enum Status {
        LOCAL, PENDANT, DOING, DONE, CANCELLED, DELIVERED;

        public Status nextStatus(Order order) {
            List<NameValuePair> params = new Vector<NameValuePair>();
            params.add(new BasicNameValuePair("uuidOrden", order.getId()));
            switch (this) {
                case LOCAL:
                    return PENDANT;
                case PENDANT:
                    RestService.callPostService(null, WellKnownMethods.OrderDoing, params);
                    return DOING;
                case DOING:
                    params.add(new BasicNameValuePair("username", BackendHelper.getLoggedUser()));
                    RestService.callPostService(null, WellKnownMethods.OrderDone, params);
                    return DONE;
                case DONE:
                    RestService.callPostService(null, WellKnownMethods.OrderDelivered, params);
                    return DELIVERED;
                default:
                    return null;
            }
        }

        @Override
        public String toString() {
            switch (this) {
                case LOCAL:
                    return "Local";
                case PENDANT:
                    return "Pendiente";
                case DOING:
                    return "En Preparacion";
                case DONE:
                    return "Terminado";
                case CANCELLED:
                    return "Cancelado";
                case DELIVERED:
                    return "Entregado";
            }
            return super.toString();
        }
    }

    private ItemProduct item;
    private Addition addition;
    private NameValuePair price;
    private int count;
    private String id;
    private String user;
    private Status status;
    private String notes;
    private List<Status> statusList;
    private Date created;
    private int tableNumber;
    private boolean isRejected;

    private Order() {
        this.statusList = new Vector<Status>();
        created = new Date();
        tableNumber = 0;
    }

    public static Order buildOrder(JSONObject json) {
        try{
            Order orden = new Order();
            orden.count = 1;
            orden.id = json.getString(ID);
            Menu menu = Menu.getInstance();
            long rubroId = json.getLong(RUBRO);
            long subRubroId = json.getLong(SUBRUBRO);
            long productId = json.getLong(PRODUCT);
            orden.item = menu.getProductById(rubroId, subRubroId, productId);
            try{
                long additionId = json.getLong(ADDITION);
                orden.addition = menu.getAdditionById(rubroId, subRubroId, additionId);
            } catch (Exception e) {
                orden.addition = null;
            }
            JSONObject price = json.getJSONObject(PRICE);
            orden.price = new BasicNameValuePair(price.getString(WellKnownKeys.DESCRIPTION), price.getString(WellKnownKeys.VALUE));
            String status = json.getString(STATUS);
            if (status.equals("Local")) {
                orden.status = Status.LOCAL;
            } else if (status.equals("Pendiente")) {
                orden.status = Status.PENDANT;
            } else if (status.equals("En Preparacion")) {
                orden.status = Status.DOING;
            } else if (status.equals("Terminado")) {
                orden.status = Status.DONE;
            } else if (status.equals("Cancelado")) {
                orden.status = Status.CANCELLED;
            } else if (status.equals("Entregado")) {
                orden.status = Status.DELIVERED;
            } else {
                return null;
            }
            orden.statusList.add(0, orden.status);
            orden.notes = json.getString(NOTES);
            try {
                orden.created = new Date(json.getJSONObject("creado").getLong("tiempo"));
                orden.tableNumber = json.getInt("mesa");
                orden.user = json.getString("username");
                orden.isRejected = json.getBoolean("fueRechazada");
            } catch (Exception e) {
                orden.isRejected = false;
            }
            return orden;
        } catch (Exception e) {

        }
        return null;
    }

    public Order(ItemProduct item, Addition addition, NameValuePair price) {
        this.price = price;
        this.item = item;
        this.addition = addition;
        this.id = UUID.randomUUID().toString();
        this.status = Status.LOCAL;
        this.statusList = new Vector<Status>();
        this.tableNumber = 0;
        this.created = new Date();
    }

    public void setNotes(String notes){
        this.notes = notes;
    }

    public String getUser() {
        return this.user;
    }

    public String getId() {
        return id;
    }
    public float getPrice() {
        return (addition != null) ?  getCount() * (Float.parseFloat(price.getValue()) + addition.getPrice()) : getCount() * Float.parseFloat(price.getValue());
    }

    @Override
    public String toString() {
        if (price.getName().equals("-")) {
            return item.toString() + ((addition != null) ? " con " + addition.toString() : "");
        }
        return item.toString() + " (" + price.getName() + ") " + ((addition != null) ? " con " + addition.toString() : "");
    }

    public void setCount(int count) {
        this.count = count;
        this.statusList = new Vector<Status>();
        for (int i = 0; i < count; i++) {
            this.statusList.add(Status.LOCAL);
        }
    }

    public int getCount() {
        int count = 0;
        for(int i = 0 ; i < this.count; i++) {
            if (!getStatus(i).equals(Status.CANCELLED.toString())) {
                count++;
            }
        }
        return count;
    }

    @Override
    public boolean equals(Object o) {
        return o.getClass() == this.getClass() && (((Order)o).id.equals(this.id) || ((Order)o).id.contains(this.id));
    }

    @Override
    public Object clone() {
        Order clone = new Order(item, addition, price);
        clone.id = this.id;
        clone.count = this.count;
        return clone;
    }

    public void stageCompleted() {
        this.status = status.nextStatus(this);
        this.statusList = new Vector<Status>();
        for (int i = 0; i < count; i++) {
            this.statusList.add(status);
        }
    }

    public void cancel() {
        List<NameValuePair> params = new Vector<NameValuePair>();
        params.add(new BasicNameValuePair("uuidOrden", this.getId()));
        RestService.callPostService(null, WellKnownMethods.OrderCancel, params);
        this.status = Status.CANCELLED;
    }

    public boolean isLocal() {
        return this.status == Status.LOCAL;
    }

    public boolean isPendant() {
        return this.status == Status.PENDANT;
    }

    public boolean isDelivered() {
        return this.status == Status.DELIVERED;
    }

    public boolean isInProgress() {
        return this.status == Status.DOING;
    }

    public boolean isConcluded() {
        return this.status == Status.DONE;
    }
    
    public boolean isCancelled() {
        return this.status == Status.CANCELLED;
    }

    public boolean isRejected() {
        return this.isRejected;
    }

    public Status getStatus(){
        return this.status;
    }

    public String getStatus(int i){
        return (statusList.size() > i) ? statusList.get(i).toString() : this.status.toString();
    }

    public Status getStatusEnum(int i){
        return statusList.get(i);
    }

    public void setStatus(int i, Status status){
        statusList.remove(i);
        statusList.add(i, status);
    }

    public void setStatus(Status status){
        this.status = status;
        this.statusList = new Vector<Status>();
        for (int i = 0; i < count; i++) {
            this.statusList.add(status);
        }
    }


    public JSONObject toJSON() {
        JSONObject json = new JSONObject();
        try {
            json.put(ID, this.id);
            json.put(RUBRO, item.getParent().getParent().getId());
            json.put(SUBRUBRO, item.getParent().getId());
            json.put(PRODUCT, item.getId());
            json.put(NOTES, this.notes);
            if (addition != null)  {
                json.put(ADDITION, addition.getId());
            }
            switch (this.status) {
                case LOCAL:
                    json.put(STATUS, "Local");
                    break;
                case PENDANT:
                    json.put(STATUS, "Pendiente");
                    break;
                case DOING:
                    json.put(STATUS, "EnPreparacion");
                    break;
                case DONE:
                    json.put(STATUS, "Terminado");
                    break;
                case DELIVERED:
                    json.put(STATUS, "Entregado");
                    break;
                default:
                    json.put(STATUS, "Cancelado");
                    break;
            }
            JSONObject price = new JSONObject();
            price.put(WellKnownKeys.DESCRIPTION, this.price.getName());
            price.put(WellKnownKeys.VALUE, this.price.getValue());
            json.put(PRICE, price);
        } catch (JSONException e) {
        }
        return json;
    }

    public String getTime() {
        long millis = (new Date()).getTime() - created.getTime();
        long Hours = millis/(1000 * 60 * 60);
        long Mins = (millis % (1000*60*60)) / (1000*60);
        return Hours + ":" + new DecimalFormat("00").format(Mins);
    }

    public String getTableNumber() {
        return Integer.toString(this.tableNumber);
    }

}
