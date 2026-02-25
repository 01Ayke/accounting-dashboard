namespace App.Models;

public class LoginResponse
{
    public string? Token { get; set; }     // Le token JWT (clé "sID" du backend)
    public string? SID { get; set; }       // Alternative pour "sID"
    public string? Status { get; set; }
    public string? Message { get; set; }
    public int Code { get; set; }
}
