package ar.com.glasit.rom.Service;

public class WellKnownMethods {
	/* Menu */
    public final static String GetMenu= "restaurant/menu";
    public final static String GetTables= "restaurant/mesas";
    public final static String GetNotifications= "notificaciones";
    public final static String GetOrders= "orden/cocina";
    public final static String NewOrder= "orden/alta";
    public final static String Login= "autenticacion/";
    public final static String GetMyOrders= "pedido/mozo";
    public final static String GetOrdersByTable= "pedido/byMesa";
    public final static String OpenTable= "pedido/apertura";
	public final static String CloseTable= "pedido/cierre";
	public final static String OrderDoing= "orden/preparando";
	public final static String OrderDone= "orden/terminado";
	public final static String OrderCancel= "orden/cancelado";
	public final static String OrderDelivered= "orden/entregado";
	public final static String OrderRejected= "orden/rechazado";
}