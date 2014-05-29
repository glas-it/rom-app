package ar.com.glasit.rom.Service;

public class WellKnownMethods {
	/* Menu */
    public final static String GetMenu= "restaurant/menu";
    public final static String GetTables= "restaurant/mesas";
    public final static String GetFreeTables= "restaurant/mesasDisponibles";
    public final static String GetOrders= "orden/all";
    public final static String NewOrder= "orden/alta";
    public final static String Login= "autenticacion/";
    public final static String OpenTable= "pedido/apertura";
    public final static String CloseTable= "pedido/cierre";
    public final static String OrderDoing= "orden/preparando";
    public final static String OrderDone= "orden/terminado";
    public final static String OrderCancel= "orden/cancelado";
    public final static String OrderDelivered= "orden/entregado";
    public final static String OrderRejected= "orden/rechazado";

    public final static String GetTable= "pedido/byMesa";
    public final static String TakeTable= "pedido/cambioMozo";
    public final static String JoinTable= "pedido/agregarMesa";
    public final static String UnjoinTable= "pedido/quitarMesa";
    public final static String NewFellow= "pedido/agregarComensales";
    public final static String GetNotifications = "restaurant/notificacion";
    public final static String ValidateCoupon = "pedido/validar";
    public final static String AddCoupon = "pedido/agregarPromocion";
}