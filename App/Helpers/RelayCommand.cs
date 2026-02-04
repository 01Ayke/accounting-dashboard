using System;
using System.Windows.Input;

namespace App.Helpers
{
    /// <summary>
    /// Représente une implémentation simple de <see cref="ICommand"/> permettant de lier
    /// une action à exécuter depuis le ViewModel dans WPF.
    /// </summary>
    /// <remarks>
    /// Cette classe est généralement utilisée dans le pattern MVVM pour déléguer
    /// les commandes des boutons ou autres contrôles UI à des méthodes du ViewModel.
    /// </remarks>
    public class RelayCommand : ICommand
    {
        private readonly Action _execute;
        private readonly Func<bool>? _canExecute;

        /// <summary>
        /// Initialise une nouvelle instance de <see cref="RelayCommand"/>.
        /// </summary>
        /// <param name="execute">L'action à exécuter lorsque la commande est invoquée.</param>
        /// <param name="canExecute">
        /// Une fonction optionnelle qui détermine si la commande peut s'exécuter.
        /// Si null, la commande est toujours exécutable.
        /// </param>
        /// <exception cref="ArgumentNullException">Si <paramref name="execute"/> est null.</exception>
        public RelayCommand(Action execute, Func<bool>? canExecute = null)
        {
            _execute = execute ?? throw new ArgumentNullException(nameof(execute));
            _canExecute = canExecute;
        }

        /// <summary>
        /// Détermine si la commande peut s'exécuter.
        /// </summary>
        /// <param name="parameter">Paramètre optionnel (non utilisé dans cette implémentation).</param>
        /// <returns>
        /// <c>true</c> si la commande peut s'exécuter ; sinon <c>false</c>.
        /// </returns>
        public bool CanExecute(object? parameter)
        {
            return _canExecute == null || _canExecute();
        }

        /// <summary>
        /// Exécute l'action associée à la commande.
        /// </summary>
        /// <param name="parameter">Paramètre optionnel (non utilisé dans cette implémentation).</param>
        public void Execute(object? parameter)
        {
            _execute();
        }

        /// <summary>
        /// Événement déclenché lorsque la capacité d'exécution de la commande change.
        /// </summary>
        /// <remarks>
        /// Cet événement est automatiquement lié à <see cref="CommandManager.RequerySuggested"/>
        /// pour que WPF mette à jour l'état des boutons et autres contrôles liés à cette commande.
        /// </remarks>
        public event EventHandler? CanExecuteChanged
        {
            add => CommandManager.RequerySuggested += value;
            remove => CommandManager.RequerySuggested -= value;
        }
    }
}
