using System.Globalization;
using System.Windows.Data;
using App.Models;
using App.ViewModels;

namespace App.Helpers.Converters;

public class DossierDetailViewModelConverter : IValueConverter
{
    public object? Convert(object? value, Type targetType, object? parameter, CultureInfo culture)
    {
        if (value is DossierFactice dossier)
        {
            return new DossierDetailViewModel { Dossier = dossier };
        }
        return null;
    }

    public object ConvertBack(object? value, Type targetType, object? parameter, CultureInfo culture)
    {
        throw new NotImplementedException();
    }
}
